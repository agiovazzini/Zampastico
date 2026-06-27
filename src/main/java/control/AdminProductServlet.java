package control;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import dao.ProdottoDAOImp;
import dao.VarianteProdottoDAOImp;
import dao.CategoriaDAOImp;
import model.CategoriaBEAN;
import model.ProdottoBEAN;
import model.VarianteProdottoBEAN;
import model.UtenteBEAN;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/admin/manageProducts")
@jakarta.servlet.annotation.MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class AdminProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProdottoDAOImp prodottoDAO;
    private VarianteProdottoDAOImp varianteDAO;
    private CategoriaDAOImp categoriaDAO;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
        if (ds == null) throw new ServletException("DataSource non disponibile nel contesto");
        prodottoDAO = new ProdottoDAOImp(ds);
        varianteDAO = new VarianteProdottoDAOImp(ds);
        categoriaDAO = new CategoriaDAOImp(ds);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UtenteBEAN utente = (session != null) ? (UtenteBEAN) session.getAttribute("utenteLoggato") : null;
        if (utente == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Accesso negato.");
            return;
        }
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                handleCreate(request, response, session);
                return;
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            JSONObject jsonResponse = new JSONObject();
            boolean success = false;
            switch (action) {
            case "getProductsByCategory":
                List<ProdottoBEAN> prodotti = prodottoDAO.doRetrieveByCategoria(Integer.parseInt(request.getParameter("idCategoria")));
                JSONArray arrProd = new JSONArray();
                for (ProdottoBEAN p: prodotti) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", p.getId());
                    obj.put("nome", p.getNome());
                    obj.put("marca", p.getMarca() != null ? p.getMarca() : "");
                    obj.put("attivo", p.isAttivo());
                    obj.put("path", p.getPath());
                    arrProd.put(obj);
                }
                jsonResponse.put("success", true);
                jsonResponse.put("prodotti", arrProd);
                break;
            case "getVariantsByProduct":
                List<VarianteProdottoBEAN> varianti = varianteDAO.doRetrieveByProdotto(Integer.parseInt(request.getParameter("idProdotto")));
                JSONArray arrVar = new JSONArray();
                for (VarianteProdottoBEAN v: varianti) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", v.getIdVarianteProdotto());
                    obj.put("formato", v.getFormato());
                    obj.put("prezzo", v.getPrezzoListino());
                    obj.put("disponibile", v.isDisponibile());
                    obj.put("path", v.getPath());
                    arrVar.put(obj);
                }
                jsonResponse.put("success", true);
                jsonResponse.put("varianti", arrVar);
                break;
            case "getProductDetails":
                ProdottoBEAN pInfo = prodottoDAO.doRetrieveByKey(Integer.parseInt(request.getParameter("idProdotto")));
                if (pInfo != null) {
                    jsonResponse.put("success", true);
                    jsonResponse.put("id", pInfo.getId());
                    jsonResponse.put("nome", pInfo.getNome());
                    jsonResponse.put("marca", pInfo.getMarca());
                    jsonResponse.put("descrizione", pInfo.getDescrizione());
                    jsonResponse.put("idCategoria", pInfo.getIdCategoria());
                    jsonResponse.put("attivo", pInfo.isAttivo());
                    jsonResponse.put("path", pInfo.getPath());
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Prodotto non trovato.");
                }
                break;
            case "updateProduct":
                ProdottoBEAN pUpdate = prodottoDAO.doRetrieveByKey(Integer.parseInt(request.getParameter("idProdotto")));
                if (pUpdate != null) {
                    pUpdate.setNome(request.getParameter("nome"));
                    pUpdate.setMarca(request.getParameter("marca"));
                    pUpdate.setDescrizione(request.getParameter("descrizione"));
                    pUpdate.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));
                    pUpdate.setAttivo(request.getParameter("prodottoAttivo") != null);
                    boolean removeImgProd = "true".equals(request.getParameter("removeImmagineProdotto"));
                    String[] newImgData = processImageUpload(request.getPart("immagineProdotto"));
                    if (newImgData != null) {
                        pUpdate.setPath(newImgData[0]);
                        pUpdate.setMimeType(newImgData[1]);
                        prodottoDAO.doUpdateImage(pUpdate.getId(), newImgData[0], newImgData[1]);
                    } else if (removeImgProd) {
                        pUpdate.setPath(null);
                        pUpdate.setMimeType(null);
                        prodottoDAO.doUpdateImage(pUpdate.getId(), null, null);
                    }

                    success = prodottoDAO.doUpdate(pUpdate);
                    jsonResponse.put("success", success);
                    if (!success) jsonResponse.put("message", "Errore del database.");
                }
                break;
            case "getVariantDetails":
                VarianteProdottoBEAN vInfo = varianteDAO.doRetrieveByKey(Integer.parseInt(request.getParameter("idVariante")));
                if (vInfo != null) {
                    jsonResponse.put("success", true);
                    jsonResponse.put("id", vInfo.getIdVarianteProdotto());
                    jsonResponse.put("formato", vInfo.getFormato());
                    jsonResponse.put("prezzo", vInfo.getPrezzoListino());
                    jsonResponse.put("disponibile", vInfo.isDisponibile());
                    jsonResponse.put("path", vInfo.getPath());
                }
                break;
            case "updateVariant":
                VarianteProdottoBEAN vUpdate = varianteDAO.doRetrieveByKey(Integer.parseInt(request.getParameter("idVariante")));
                if (vUpdate != null) {
                    vUpdate.setFormato(request.getParameter("formato"));
                    vUpdate.setPrezzoListino(Double.parseDouble(request.getParameter("prezzo")));
                    vUpdate.setDisponibile(request.getParameter("varianteDisponibile") != null);
                    
                    boolean removeImgVar = "true".equals(request.getParameter("removeImmagineVariante"));
                    String[] newVarImgData = processImageUpload(request.getPart("immagineVariante"));
                    
                    if (newVarImgData != null) {
                        vUpdate.setPath(newVarImgData[0]);
                        vUpdate.setMimeType(newVarImgData[1]);
                        varianteDAO.doUpdateImage(vUpdate.getIdVarianteProdotto(), newVarImgData[0], newVarImgData[1]);
                    } else if (removeImgVar) {
                        vUpdate.setPath(null);
                        vUpdate.setMimeType(null);
                        varianteDAO.doUpdateImage(vUpdate.getIdVarianteProdotto(), null, null);
                    }
                    
                    success = varianteDAO.doUpdate(vUpdate);
                    jsonResponse.put("success", success);
                    if (!success) jsonResponse.put("message", "Errore aggiornamento variante.");
                }
                break;
            case "createVariant":
                VarianteProdottoBEAN nuovaVariante = new VarianteProdottoBEAN();
                nuovaVariante.setIdProdotto(Integer.parseInt(request.getParameter("idProdotto")));
                nuovaVariante.setFormato(request.getParameter("formato"));
                nuovaVariante.setPrezzoListino(Double.parseDouble(request.getParameter("prezzo")));
                nuovaVariante.setDisponibile(request.getParameter("varianteDisponibile") != null);
                String[] createVarImg = processImageUpload(request.getPart("immagineVariante"));
                if (createVarImg != null) {
                    nuovaVariante.setPath(createVarImg[0]);
                    nuovaVariante.setMimeType(createVarImg[1]);
                }
                
                try {
                    varianteDAO.doSave(nuovaVariante);
                    jsonResponse.put("success", true);
                    jsonResponse.put("message", "Variante aggiunta con successo!");
                } catch (Exception e) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Errore database nella creazione.");
                }
                break;
            case "toggleStatus":
                String typeStatus = request.getParameter("type");
                int idStatus = Integer.parseInt(request.getParameter("id"));
                boolean newStatus = Boolean.parseBoolean(request.getParameter("status"));
                if ("prodotto".equals(typeStatus)) {
                    success = prodottoDAO.doUpdateStato(idStatus, newStatus);
                } else if ("variante".equals(typeStatus)) {
                    VarianteProdottoBEAN var = varianteDAO.doRetrieveByKey(idStatus);
                    if (var != null) {
                        var.setDisponibile(newStatus);
                        success = varianteDAO.doUpdate(var);
                    }
                }
                jsonResponse.put("success", success);
                break;
            case "hardDelete":
                String typeDel = request.getParameter("type");
                int idDel = Integer.parseInt(request.getParameter("id"));
                if ("variante".equals(typeDel)) {
                    success = varianteDAO.doDelete(idDel);
                } else if ("prodotto".equals(typeDel)) {
                    success = deleteProdottoEVarianti(idDel);
                } else if ("categoria".equals(typeDel)) {
                    success = categoriaDAO.doDelete(idDel);
                }
                jsonResponse.put("success", success);
                if (!success) jsonResponse.put("message", "Impossibile eliminare l'elemento.");
                break;
            default:
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Azione non riconosciuta.");
                break;
            }
            out.print(jsonResponse);
        } catch (Exception e) {
            if ("create".equals(action)) {
                session.setAttribute("feedback", "Errore critico durante il salvataggio.");
                response.sendRedirect(request.getContextPath() + "/admin/product");
            } else {
                response.setContentType("application/json");
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Errore interno del server.");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse);
            }
        }
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String nome = request.getParameter("nome");
        String marca = request.getParameter("marca");
        if (prodottoDAO.checkEsiste(nome, marca)) {
            session.setAttribute("feedback", "Errore: Esiste già un prodotto con questo Nome e Brand.");
            response.sendRedirect(request.getContextPath() + "/admin/product");
            return;
        }
        int idCategoria;
        String catParam = request.getParameter("idCategoria");
        if ("new".equals(catParam)) {
            String nomeSottocategoria = request.getParameter("nuovaCategoria").trim();
            String nomeSupercategoria = request.getParameter("nuovaSupercategoria").trim();
            int idSuper = 0;
            if (nomeSupercategoria != null && !nomeSupercategoria.isEmpty()) {
                CategoriaBEAN superCat = categoriaDAO.doRetrieveByName(nomeSupercategoria);
                if (superCat != null) {
                    idSuper = superCat.getIdCategoria();
                } else {
                    superCat = new CategoriaBEAN();
                    superCat.setNome(nomeSupercategoria);
                    superCat.setidSuper(0);
                    categoriaDAO.doSave(superCat);
                    idSuper = superCat.getIdCategoria();
                }
            }
            CategoriaBEAN subCat = categoriaDAO.doRetrieveByNameAndIdSuper(nomeSottocategoria, idSuper);
            if (subCat != null) {
                session.setAttribute("feedback", "Errore: La categoria '" + nomeSottocategoria + "' esiste già in questo ramo.");
                response.sendRedirect(request.getContextPath() + "/admin/product");
                return;
            } else {
                subCat = new CategoriaBEAN();
                subCat.setNome(nomeSottocategoria);
                subCat.setidSuper(idSuper);
                categoriaDAO.doSave(subCat);
                idCategoria = subCat.getIdCategoria();
            }
        } else {
            idCategoria = Integer.parseInt(catParam);
        }
        ProdottoBEAN p = new ProdottoBEAN();
        p.setNome(nome);
        p.setMarca(marca);
        p.setDescrizione(request.getParameter("descrizione"));
        p.setIdCategoria(idCategoria);
        p.setAttivo(request.getParameter("prodottoAttivo") != null);
        String[] imgProd = processImageUpload(request.getPart("immagineProdotto"));
        if (imgProd != null) {
            p.setPath(imgProd[0]);
            p.setMimeType(imgProd[1]);
        }
        prodottoDAO.doSave(p);
        VarianteProdottoBEAN v = new VarianteProdottoBEAN();
        v.setIdProdotto(p.getId());
        v.setFormato(request.getParameter("formato"));
        v.setPrezzoListino(Double.parseDouble(request.getParameter("prezzo")));
        v.setDisponibile(request.getParameter("varianteDisponibile") != null);
        String[] imgVar = processImageUpload(request.getPart("immagineVariante"));
        if (imgVar != null) {
            v.setPath(imgVar[0]);
            v.setMimeType(imgVar[1]);
        }
        varianteDAO.doSave(v);
        session.setAttribute("feedback", "Prodotto e variante salvati con successo!");
        response.sendRedirect(request.getContextPath() + "/admin/product");
    }

    private String[] processImageUpload(Part filePart) throws IOException {
        if (filePart == null || filePart.getSubmittedFileName() == null || filePart.getSubmittedFileName().isEmpty() || filePart.getSize() == 0) {
            return null;
        }
        String mimeType = filePart.getContentType();
        String uniqueFileName = buildUniqueFileName(filePart);
        String uploadDirStr = getServletContext().getRealPath("/uploads");
        File uploadDir = new File(uploadDirStr);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String uploadPath = uploadDirStr + File.separator + uniqueFileName;
        filePart.write(uploadPath);
        return new String[]{uploadPath, mimeType};
    }

    private boolean deleteProdottoEVarianti(int idProdotto) throws Exception {
        List < VarianteProdottoBEAN > varianti = varianteDAO.doRetrieveByProdotto(idProdotto);
        for (VarianteProdottoBEAN v: varianti) {
            if (!varianteDAO.doDelete(v.getIdVarianteProdotto())) return false;
        }
        return prodottoDAO.doDelete(idProdotto);
    }
    
    private String buildUniqueFileName(Part part) {
        String originalName = part.getSubmittedFileName();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }
        return UUID.randomUUID() + extension;
    }
}
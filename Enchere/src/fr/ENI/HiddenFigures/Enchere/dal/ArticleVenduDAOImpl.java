package fr.ENI.HiddenFigures.Enchere.dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.ENI.HiddenFigures.Enchere.bo.ArticleVendu;
import fr.ENI.HiddenFigures.Enchere.bo.Utilisateur;


public class ArticleVenduDAOImpl implements ArticleVenduDAO {
	private String INSERT = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, refPhoto) "
			+ " VALUES (?,?,?,?,?,?,?,?,?)";
	private String SELECT_ALL = "SELECT * FROM ARTICLES_VENDUS";
	private String SELECT_ONE = "SELECT * FROM ARTICLES_VENDUS WHERE no_article=?";
	private String SELECT_BY_ETAT_VENTE_ENCOURS = "SELECT * FROM  ARTICLES_VENDUS where GETDATE() BETWEEN "
			+ " date_debut_encheres AND date_fin_encheres";
	//private String SELECT_BY_ETAT_VENTE_NONDEBUTE = "SELECT * FROM  ARTICLES_VENDUS where DATEDIFF(day, GETDATE(),date_debut_encheres) >0";
	//private String SELECT_BY_ETAT_VENTE_TERMINE = "SELECT * FROM  ARTICLES_VENDUS where DATEDIFF(day, date_fin_encheres,GETDATE()) >0";
	private String select_Utilisayeur_By_Article = "Select pseudo from ARTICLES_VENDUS a inner Join UTILISATEURS u on u.no_utilisateur = ?";
	private String DELETE_BY_NO_UTILISATEUR = "DELETE FROM  ARTICLES_VENDUS where  no_utilisateur=?";
	private String DELETE_BY_NO_CATEGORIE = "DELETE FROM  ARTICLES_VENDUS where  no_categorie=?";

	private String DELETE_BY_ID = "DELETE FROM  ARTICLES_VENDUS where  no_article=?";
	
	
	private String SELECT_BY_NO_UTILISATEUR = "SELECT * FROM  ARTICLES_VENDUS where  no_utilisateur=?";
	private String SELECT_BY_NO_CATEGORIE = "SELECT * FROM  ARTICLES_VENDUS where  no_categorie=?"; //for admin donc ik faut tous les comptes même comptes désactivés
	
	
	private String UPDATE_PRIX_VENTE = "UPDATE ARTICLES_VENDUS  SET prix_vente =? where no_article =?";
	private String UPDATE_REF_PHOTO = "UPDATE ARTICLES_VENDUS  SET refPhoto =? where no_article =?";
	
	private String UPDATE= "UPDATE ARTICLES_VENDUS  SET nom_article=?, description=?, date_debut_encheres=?, date_fin_encheres =?,"
			+ " prix_initial=?, prix_vente=?, no_utilisateur=?, no_categorie=? where no_article =?";
	
	UtilisateurDAO utilisateurDAO = DAOFactory.getUtilisateurDAO();
	
	public void updatePrixVente(Integer noArticle, Integer new_prixVente) throws DALException {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(UPDATE_PRIX_VENTE);
			stmt.setInt(1, new_prixVente)  ;  
			stmt.setInt(2,  noArticle)  ;
			stmt.executeUpdate(); 
		} catch (Exception e) {
			throw new DALException("Couche DAL - problème dans la modification de prix vente d'un article");
		}
		
	}
	public void updateRefPhoto(Integer noArticle, String new_refPhoto) throws DALException {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(UPDATE_REF_PHOTO);
			stmt.setString(1, new_refPhoto)  ;  
			stmt.setInt(2,  noArticle)  ;
			stmt.executeUpdate(); 
		} catch (Exception e) {
			throw new DALException("Couche DAL - problème dans la modification de refPhoto d'un article");
		}
		
	}
	
	public List<ArticleVendu> selectByEtatVenteEnCours() throws DALException {
		List<ArticleVendu> result = new ArrayList<ArticleVendu>();
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(SELECT_BY_ETAT_VENTE_ENCOURS);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ArticleVendu articleVendu = new ArticleVendu();
				articleVendu.setNoArticle(rs.getInt("no_article"));
				articleVendu.setNomArticle(rs.getString("nom_article"));
				articleVendu.setDescription(rs.getString("description")); 
				
				String dateDebutEncheresString = rs.getString("date_debut_encheres");
				LocalDate dateDebutEncheresLocalDate = Date.valueOf(dateDebutEncheresString).toLocalDate();
				
				articleVendu.setDateDebutEncheres(dateDebutEncheresLocalDate);
				
				String dateFinEncheresString = rs.getString("date_fin_encheres");
				LocalDate dateFinEncheresLocalDate = Date.valueOf(dateFinEncheresString).toLocalDate();
				
				articleVendu.setDateFinEncheres(dateFinEncheresLocalDate);
				
				articleVendu.setMiseAprix(rs.getInt("prix_initial"));
				articleVendu.setPrixVente(rs.getInt("prix_vente"));
				articleVendu.setNoUtilisateur(rs.getInt("no_utilisateur"));
				articleVendu.setNoCategorie(rs.getInt("no_categorie"));
				
				articleVendu.setRefPhoto(rs.getString("refPhoto")); 
	
				// filtre que les utilisateurs active
				Utilisateur utilisateur = utilisateurDAO.getUtilisateur(rs.getInt("no_utilisateur"));
				if("A".equals(utilisateur.getEtatCompte())) {
					result.add(articleVendu);
				}
				
			}
		} catch (Exception e) {
			throw new DALException("Couche DAL - Problème dans la selection des articles par l'état de vente en cours");
		}
		return result;
	}
//	public List<ArticleVendu> selectByEtatVenteNonDebute() throws DALException {
//		List<ArticleVendu> result = new ArrayList<ArticleVendu>();
//		try (Connection cnx = ConnectionProvider.getConnection()) {
//			PreparedStatement stmt = cnx.prepareStatement(SELECT_BY_ETAT_VENTE_NONDEBUTE);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()) {
//				ArticleVendu articleVendu = new ArticleVendu();
//				articleVendu.setNoArticle(rs.getInt("no_article"));
//				articleVendu.setNomArticle(rs.getString("nom_article"));
//				articleVendu.setDescription(rs.getString("description")); 
//				
//				String dateDebutEncheresString = rs.getString("date_debut_encheres");
//				LocalDate dateDebutEncheresLocalDate = Date.valueOf(dateDebutEncheresString).toLocalDate();
//				
//				articleVendu.setDateDebutEncheres(dateDebutEncheresLocalDate);
//				
//				String dateFinEncheresString = rs.getString("date_fin_encheres");
//				LocalDate dateFinEncheresLocalDate = Date.valueOf(dateFinEncheresString).toLocalDate();
//				
//				articleVendu.setDateFinEncheres(dateFinEncheresLocalDate);
//				
//				articleVendu.setMiseAprix(rs.getInt("prix_initial"));
//				articleVendu.setPrixVente(rs.getInt("prix_vente"));
//				articleVendu.setNoUtilisateur(rs.getInt("no_utilisateur"));
//				articleVendu.setNoCategorie(rs.getInt("no_categorie"));
//	
//				
//				
//				result.add(articleVendu);
//			}
//		} catch (Exception e) {
//			throw new DALException("Couche DAL - Problème dans la selection des utilisateurs par l'état de vente non debute");
//		}
//		return result;
//	}
//	
//	public List<ArticleVendu> selectByEtatVenteTermine() throws DALException {
//		List<ArticleVendu> result = new ArrayList<ArticleVendu>();
//		try (Connection cnx = ConnectionProvider.getConnection()) {
//			PreparedStatement stmt = cnx.prepareStatement(SELECT_BY_ETAT_VENTE_TERMINE);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()) {
//				ArticleVendu articleVendu = new ArticleVendu();
//				articleVendu.setNoArticle(rs.getInt("no_article"));
//				articleVendu.setNomArticle(rs.getString("nom_article"));
//				articleVendu.setDescription(rs.getString("description")); 
//				
//				String dateDebutEncheresString = rs.getString("date_debut_encheres");
//				LocalDate dateDebutEncheresLocalDate = Date.valueOf(dateDebutEncheresString).toLocalDate();
//				
//				articleVendu.setDateDebutEncheres(dateDebutEncheresLocalDate);
//				
//				String dateFinEncheresString = rs.getString("date_fin_encheres");
//				LocalDate dateFinEncheresLocalDate = Date.valueOf(dateFinEncheresString).toLocalDate();
//				
//				articleVendu.setDateFinEncheres(dateFinEncheresLocalDate);
//				
//				articleVendu.setMiseAprix(rs.getInt("prix_initial"));
//				articleVendu.setPrixVente(rs.getInt("prix_vente"));
//				articleVendu.setNoUtilisateur(rs.getInt("no_utilisateur"));
//				articleVendu.setNoCategorie(rs.getInt("no_categorie"));
//	
//				
//				
//				result.add(articleVendu);
//			}
//		} catch (Exception e) {
//			throw new DALException("Couche DAL - Problème dans la selection des utilisateurs par l'état de vente termine");
//		}
//		return result;
//	}
//	
	
	public String selectUtilisateurByArticle(ArticleVendu article ) throws DALException {
		String pseudo= null; 
		try (Connection cnx = ConnectionProvider.getConnection()) {
			
			PreparedStatement stmt = cnx.prepareStatement(select_Utilisayeur_By_Article);
			stmt.setInt(1,  article.getNoUtilisateur())  ;  
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) { 
				pseudo =  rs.getString("pseudo");

			}
		} catch (Exception e) {
			throw new DALException("Couche DAL - Problème dans la selection des utilisateurs par l'article");
		}
		return pseudo;
	}
	public void deleteByNoUtilisateur(Integer noUtilisateur ) throws DALException {
		 
		try (Connection cnx = ConnectionProvider.getConnection()) {
			
			PreparedStatement stmt = cnx.prepareStatement(DELETE_BY_NO_UTILISATEUR);
			stmt.setInt(1, noUtilisateur )  ;  
			stmt.executeUpdate(); 
			 
		} catch (Exception e) {
			throw new DALException("Couche DAL - Problème dans la suppression des articles par noUtilisateur");
		}
		 
	}
	@Override
	public void deleteByNoCategorie(Integer noCategorie ) throws DALException {
		 
		try (Connection cnx = ConnectionProvider.getConnection()) {
			
			PreparedStatement stmt = cnx.prepareStatement(DELETE_BY_NO_CATEGORIE);
			stmt.setInt(1, noCategorie )  ;  
			stmt.executeUpdate(); 
			 
		} catch (Exception e) {
			throw new DALException("Couche DAL - Problème dans la suppression des articles par noCategorie");
		}
		 
	}
	public List<ArticleVendu> selectByNoUtilisateur(Integer noUtilisateur) throws DALException {
		List<ArticleVendu> result = new ArrayList<ArticleVendu>();
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(SELECT_BY_NO_UTILISATEUR);
			stmt.setInt(1, noUtilisateur );
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ArticleVendu articleVendu = new ArticleVendu();
				articleVendu.setNoArticle(rs.getInt("no_article"));
				articleVendu.setNomArticle(rs.getString("nom_article"));
				articleVendu.setDescription(rs.getString("description")); 
				
				String dateDebutEncheresString = rs.getString("date_debut_encheres");
				LocalDate dateDebutEncheresLocalDate = Date.valueOf(dateDebutEncheresString).toLocalDate();
				
				articleVendu.setDateDebutEncheres(dateDebutEncheresLocalDate);
				
				String dateFinEncheresString = rs.getString("date_fin_encheres");
				LocalDate dateFinEncheresLocalDate = Date.valueOf(dateFinEncheresString).toLocalDate();
				
				articleVendu.setDateFinEncheres(dateFinEncheresLocalDate);
				
				articleVendu.setMiseAprix(rs.getInt("prix_initial"));
				articleVendu.setPrixVente(rs.getInt("prix_vente"));
				articleVendu.setNoUtilisateur(rs.getInt("no_utilisateur"));
				articleVendu.setNoCategorie(rs.getInt("no_categorie"));
				
				articleVendu.setRefPhoto(rs.getString("refPhoto")); 
	
				// filtre que les utilisateurs active
				Utilisateur utilisateur = utilisateurDAO.getUtilisateur(rs.getInt("no_utilisateur"));
				if("A".equals(utilisateur.getEtatCompte())) {
					result.add(articleVendu);
				}
				
				 
			}
		} catch (Exception e) {
			throw new DALException("Couche DAL - Problème dans la selection des utilisateurs par noUtilisateur");
		}
		return result;
	}
	@Override
	public List<ArticleVendu> selectByNoCategorie(Integer noCategorie) throws DALException { //only for admin car il y a tous les articles (désactivés)
		List<ArticleVendu> result = new ArrayList<ArticleVendu>();
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(SELECT_BY_NO_CATEGORIE);
			stmt.setInt(1, noCategorie );
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ArticleVendu articleVendu = new ArticleVendu();
				articleVendu.setNoArticle(rs.getInt("no_article"));
				articleVendu.setNomArticle(rs.getString("nom_article"));
				articleVendu.setDescription(rs.getString("description")); 
				
				String dateDebutEncheresString = rs.getString("date_debut_encheres");
				LocalDate dateDebutEncheresLocalDate = Date.valueOf(dateDebutEncheresString).toLocalDate();
				
				articleVendu.setDateDebutEncheres(dateDebutEncheresLocalDate);
				
				String dateFinEncheresString = rs.getString("date_fin_encheres");
				LocalDate dateFinEncheresLocalDate = Date.valueOf(dateFinEncheresString).toLocalDate();
				
				articleVendu.setDateFinEncheres(dateFinEncheresLocalDate);
				
				articleVendu.setMiseAprix(rs.getInt("prix_initial"));
				articleVendu.setPrixVente(rs.getInt("prix_vente"));
				articleVendu.setNoUtilisateur(rs.getInt("no_utilisateur"));
				articleVendu.setNoCategorie(rs.getInt("no_categorie"));
				
				articleVendu.setRefPhoto(rs.getString("refPhoto")); 
	
//				// filtre que les utilisateurs active
//				Utilisateur utilisateur = utilisateurDAO.getUtilisateur(rs.getInt("no_utilisateur"));
//				if("A".equals(utilisateur.getEtatCompte())) {
//					result.add(articleVendu);
//				}
				result.add(articleVendu);
				 
			}
		} catch (Exception e) {
			throw new DALException("Couche DAL - Problème dans la selection des utilisateurs par noUtilisateur");
		}
		return result;
	}
	@Override
	public ArticleVendu insert(ArticleVendu articleVendu) throws DALException {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1,  articleVendu.getNomArticle())  ;  
			stmt.setString(2,  articleVendu.getDescription())  ;
			
			Date dateDebutEncheresSQL = Date.valueOf(articleVendu.getDateDebutEncheres()); 
			stmt.setDate(3, dateDebutEncheresSQL);
			
			Date dateFinEncheresSQL = Date.valueOf(articleVendu.getDateFinEncheres()); 
			stmt.setDate(4, dateFinEncheresSQL);
			
			stmt.setInt(5, articleVendu.getMiseAprix());
			if(articleVendu.getPrixVente() ==null) {
				stmt.setNull(6, Types.INTEGER);
			}
			else {
				stmt.setInt(6, articleVendu.getPrixVente());
			}
			stmt.setInt(7, articleVendu.getNoUtilisateur());
			stmt.setInt(8, articleVendu.getNoCategorie());
				
			stmt.setString(9, articleVendu.getRefPhoto());
			//stmt.setNull(9, Types.VARCHAR);
	 
			int nbRows =stmt.executeUpdate();
 	
			if (nbRows == 1) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					articleVendu.setNoArticle(rs.getInt(1));
				}
			}
				
			String new_refPhoto = "U"+articleVendu.getNoUtilisateur()+"P"+articleVendu.getNoArticle()+"-"+articleVendu.getRefPhoto();
			updateRefPhoto(articleVendu.getNoArticle(),new_refPhoto );
			articleVendu.setRefPhoto(new_refPhoto);
		} catch (Exception e) {
			throw new DALException("Couche DAL - problème dans l'insertion d'article vendu");
		}
		return articleVendu;
	}

	@Override
	public ArticleVendu getArticleVendu(Integer idArticle) throws DALException {
		ArticleVendu result = new ArticleVendu();
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(SELECT_ONE);
			stmt.setInt(1, idArticle );
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ArticleVendu articleVendu = new ArticleVendu();
				articleVendu.setNoArticle(rs.getInt("no_article"));
				articleVendu.setNomArticle(rs.getString("nom_article"));
				articleVendu.setDescription(rs.getString("description")); 
				
				String dateDebutEncheresString = rs.getString("date_debut_encheres");
				LocalDate dateDebutEncheresLocalDate = Date.valueOf(dateDebutEncheresString).toLocalDate();
				
				articleVendu.setDateDebutEncheres(dateDebutEncheresLocalDate);
				
				String dateFinEncheresString = rs.getString("date_fin_encheres");
				LocalDate dateFinEncheresLocalDate = Date.valueOf(dateFinEncheresString).toLocalDate();
				
				articleVendu.setDateFinEncheres(dateFinEncheresLocalDate);
				
				articleVendu.setMiseAprix(rs.getInt("prix_initial"));
				articleVendu.setPrixVente(rs.getInt("prix_vente"));
				articleVendu.setNoUtilisateur(rs.getInt("no_utilisateur"));
				articleVendu.setNoCategorie(rs.getInt("no_categorie"));
				
				articleVendu.setRefPhoto(rs.getString("refPhoto")); 
	
				// filtre que les utilisateurs active
				Utilisateur utilisateur = utilisateurDAO.getUtilisateur(rs.getInt("no_utilisateur"));
				if("A".equals(utilisateur.getEtatCompte())) {
					result=articleVendu;
				}
				
			}
		} catch (Exception e) {
			throw new DALException("Couche DAL - Problème dans la selection des utilisateurs par idArticle");
		}
		return result;
	}

	
	@Override
	public List<ArticleVendu> getAll() throws DALException {
		List<ArticleVendu> result = new ArrayList<ArticleVendu>();
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(SELECT_ALL);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				ArticleVendu articleVendu = new ArticleVendu();
				articleVendu.setNoArticle(rs.getInt("no_article"));
				articleVendu.setNomArticle(rs.getString("nom_article"));
				articleVendu.setDescription(rs.getString("description")); 
				
				String dateDebutEncheresString = rs.getString("date_debut_encheres");
				LocalDate dateDebutEncheresLocalDate = Date.valueOf(dateDebutEncheresString).toLocalDate();
				
				articleVendu.setDateDebutEncheres(dateDebutEncheresLocalDate);
				
				String dateFinEncheresString = rs.getString("date_fin_encheres");
				LocalDate dateFinEncheresLocalDate = Date.valueOf(dateFinEncheresString).toLocalDate();
				
				articleVendu.setDateFinEncheres(dateFinEncheresLocalDate);
				
				articleVendu.setMiseAprix(rs.getInt("prix_initial"));
				articleVendu.setPrixVente(rs.getInt("prix_vente"));
				articleVendu.setNoUtilisateur(rs.getInt("no_utilisateur"));
				articleVendu.setNoCategorie(rs.getInt("no_categorie"));
				
				articleVendu.setRefPhoto(rs.getString("refPhoto")); 
	
				//TODO: il faut récupérer l'état de vente
				// filtre que les utilisateurs active
				Utilisateur utilisateur = utilisateurDAO.getUtilisateur(rs.getInt("no_utilisateur"));
				if("A".equals(utilisateur.getEtatCompte())) {
					result.add(articleVendu);
				}
				
			}
		} catch (Exception e) {
			throw new DALException("Couche DAL - Problème dans la selection des articles vendus");
		}
		return result;
	}

	@Override
	public ArticleVendu update(ArticleVendu articleVendu) throws DALException {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(UPDATE);
			stmt.setString(1,  articleVendu.getNomArticle())  ;  
			stmt.setString(2,  articleVendu.getDescription())  ;
			
			Date dateDebutEncheresSQL = Date.valueOf(articleVendu.getDateDebutEncheres()); 
			stmt.setDate(3, dateDebutEncheresSQL);
			
			
			
			
			Date dateFinEncheresSQL = Date.valueOf(articleVendu.getDateFinEncheres()); 
			stmt.setDate(4, dateFinEncheresSQL);
			
			stmt.setInt(5, articleVendu.getMiseAprix());
			if(articleVendu.getPrixVente() ==null) {
				stmt.setNull(6, Types.INTEGER);
			}
			else {
				stmt.setInt(6, articleVendu.getPrixVente());
			}
			stmt.setInt(7, articleVendu.getNoUtilisateur());
			stmt.setInt(8, articleVendu.getNoCategorie());
			stmt.setInt(9, articleVendu.getNoArticle());
			 //TODO : update image
			stmt.executeUpdate();
			 
	
		} catch (Exception e) {
			throw new DALException("Couche DAL - problème dans UPDATE un article vendu");
		}
		return articleVendu;
	}

	@Override
	public void deleteArticleVendu(Integer idArticle) throws DALException {
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(DELETE_BY_ID);
			stmt.setInt(1,  idArticle)  ;
			DAOFactory.getEnchereDAO().deleteByNoArticle(idArticle);
			DAOFactory.getRetraitDAO().deleteByNoArticle(idArticle);
			stmt.executeUpdate(); 
		} catch (Exception e) {
			throw new DALException("Couche DAL - problème dans la suppression d'un article by idArticle");
		}
		
		
		
		 
	}
	

}

package fr.ENI.HiddenFigures.Enchere.bll;

import java.util.List;

import fr.ENI.HiddenFigures.Enchere.bo.ArticleVendu;
 

public interface ManagerArticleVendus {
	 List<ArticleVendu> getArticleByEtatVenteEnCours() throws BLLException ;
	 String getPseudoByArticle (ArticleVendu article) throws BLLException ;
	 List<ArticleVendu> getArticleByEtatNonDebute()   ;
	 List<ArticleVendu> getArticleByEtatTermine()  ;
	 List<ArticleVendu> getArticleByNomArticleContient(String motCle) ;
	 List<ArticleVendu> getArticleByNomArticleContientEtNoCategorie(String motCle, Integer noCategorie) ;
	 List<ArticleVendu> getArticleByCategorie(Integer noCategorie) ;
	 List<ArticleVendu> getArticleByNoUtilisateur(Integer noUtilisateur) ;
	 ArticleVendu getArticleVenduByNom(String nomArticle) throws BLLException ;
	/***
	 * Ajouter un article vendu
	 * @param articleVendu
	 * @return
	 * @throws BLLException
	 */
	ArticleVendu addArticleVendu(ArticleVendu articleVendu) throws BLLException;
	
	/***
	 * Lister tous les articles vendus
	 * @param articleVendu
	 * @return
	 * @throws BLLException
	 */
	
	List<ArticleVendu> getLstArticleVendus()  ;
	/***
	 * Lister un article vendu
	 * @param articleVendu
	 * @return
	 * @throws BLLException
	 */
	ArticleVendu getArticleVenduById(Integer idArticle) throws BLLException;
	/***
	 * Supprimer un article vendu
	 * @param articleVendu
	 * @return
	 * @throws BLLException
	 */
	 void deleteArticleVendu(Integer idArticle) throws BLLException;
	
	/***
	 * Verifier l'adresse de retrait de l'article
	 * @param articleVendu
	 * @return
	 * @throws B@Override
	LLException
	 */
	void verifAdresseRetrait(ArticleVendu articleVendu) throws BLLException;
	
	
	ArticleVendu getArticleVenduByNom(String nomArticle) throws BLLException;
	
	
	
}
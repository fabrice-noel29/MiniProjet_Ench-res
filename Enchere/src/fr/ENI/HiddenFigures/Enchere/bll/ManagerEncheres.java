package fr.ENI.HiddenFigures.Enchere.bll;

import java.util.List;

import fr.ENI.HiddenFigures.Enchere.bo.Enchere;

public interface ManagerEncheres {
	Enchere addEnchere(Enchere Enchere) throws BLLException;
	Enchere updateEnchere(Integer idEnchere) throws BLLException;
	List<Enchere> getLstEnchere()  ;
	Enchere getEnchere(Integer idEnchere) throws BLLException;
	Enchere deleteEnchere(Integer idEnchere) throws BLLException;
	List<Enchere> getLstEnchereOfHighestOffer();
	List<Enchere> getLstEnchereOfUserById(Integer noUtilisateur);
	List<Enchere> getLstEnchereOfHighestOfferOfUserById(Integer noUtilisateur);
	List<Enchere> getLstEnchereWonOfUserById(Integer noUtilisateur);
	
	
	
	
}

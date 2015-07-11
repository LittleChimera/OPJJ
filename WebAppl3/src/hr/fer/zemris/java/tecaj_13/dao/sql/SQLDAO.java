package hr.fer.zemris.java.tecaj_13.dao.sql;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.Unos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija o�ekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, �to zna�i da bi netko
 * prije no �to izvo�enje do�e do ove to�ke to trebao tamo postaviti.
 * U web-aplikacijama tipi�no rje�enje je konfigurirati jedan filter 
 * koji �e presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author marcupic
 */
public class SQLDAO implements DAO {

	@Override
	public List<Unos> dohvatiOsnovniPopisUnosa() throws DAOException {
		List<Unos> unosi = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, title from Poruke order by id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						Unos unos = new Unos();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unosi.add(unos);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogre�ka prilikom dohvata liste korisnika.", ex);
		}
		return unosi;
	}

	@Override
	public Unos dohvatiUnos(long id) throws DAOException {
		Unos unos = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select id, title, message, createdOn, userEMail from Poruke where id=?");
			pst.setLong(1, Long.valueOf(id));
			try {
				ResultSet rs = pst.executeQuery();
				try {
					if(rs!=null && rs.next()) {
						unos = new Unos();
						unos.setId(rs.getLong(1));
						unos.setTitle(rs.getString(2));
						unos.setMessage(rs.getString(3));
						unos.setCreatedOn(rs.getTimestamp(4));
						unos.setUserEMail(rs.getString(5));
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogre�ka prilikom dohvata korisnika.", ex);
		}
		return unos;
	}

}

package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.BaseServiceImpl;
import am.aca.orgflix.service.CastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Service layer for actor related methods
 */
@Transactional
@Service
public class CastServiceImpl extends BaseServiceImpl implements CastService {

    private CastDAO castDAO;

    @Autowired
    public void setCastDAO(CastDAO castDAO) {
        this.castDAO = castDAO;
    }

    /**
     * @see CastService#addCast(am.aca.orgflix.entity.Cast)
     */
    @Override
    public boolean addCast(Cast cast) {
        checkRequiredFields(cast.getName());
        return castDAO.addCast(cast);
    }

    /**
     * @see CastService#editCast(Cast)
     */
    @Override
    public boolean editCast(Cast cast) {
        checkRequiredFields(cast.getName());
        return castDAO.editCast(cast);
    }

    /**
     * @see CastService#listCasts()
     */
    @Override
    public List<Cast> listCasts() {
        return castDAO.listCast();
    }

    /**
     * @see CastService#getCastById(int)
     */
    @Override
    public Cast getCastById(int castId) {
        return castDAO.getCastById(castId);
    }

}
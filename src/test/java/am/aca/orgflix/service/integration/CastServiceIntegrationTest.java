package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.ServiceException;
import am.aca.orgflix.service.impl.CastServiceImpl;
import am.aca.orgflix.util.TestHelper;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Integration tests for Cast Service Layer
 */
public class CastServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CastService castService;

    @Autowired
    private TestHelper helper;

    private Cast cast;

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        helper.emptyTable(new String[]{
                "film_to_cast", "casts", "films"
        });
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_WithOscar_Success() {
        castService.addCast(new Cast("Edward Norton", true));
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_WithoutOscar_Success() {
        castService.addCast(new Cast("Edward Norton"));
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_Success() {
        cast = new Cast("Edward Norton", false);
        castService.addCast(cast);
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_NoOscar_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void addCast_NullName_Fail() {
        cast = new Cast(null);
        castService.addCast(cast);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void addCast_EmptyName_Fail() {
        cast = new Cast("");
        castService.addCast(cast);
    }


    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);

        cast.setHasOscar(true);
        boolean status = castService.editCast(cast);
        int size = castService.listCasts().size();

        Assert.assertTrue(status);
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void editCast_BadName_Fail() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);

        cast.setName(null);
        castService.editCast(cast);
    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test (expected = ServiceException.class)
    public void editCast_NotExisting_Fail() {
        cast = new Cast();
        castService.editCast(cast);
    }

    /**
     * @see CastServiceImpl#listCasts()
     */
    @Test
    public void listCasts_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast = new Cast("Emma Stone", true);
        castService.addCast(cast);

        List<Cast> actualCast = castService.listCasts();
        Assert.assertEquals(2, actualCast.size());
        Assert.assertEquals(cast, actualCast.get(1));
    }

    /**
     * @see CastServiceImpl#listCasts()
     */
    @Test
    public void listCasts_Empty_Fail() {
        int size = castService.listCasts().size();
        Assert.assertEquals(0, size);
    }
}

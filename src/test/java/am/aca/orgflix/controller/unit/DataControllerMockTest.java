package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.ActorDataController;
import am.aca.orgflix.controller.FilmDataController;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests for Data Controller
 */
public class DataControllerMockTest extends BaseUnitTest {
    @Autowired
    private FilmDataController filmDataController;
    @Autowired
    private ActorDataController actorDataController;

    @Mock
    private UserService userServiceMock;
    @Mock
    private CastService castServiceMock;
    @Mock
    private FilmService filmServiceMock;
    @Mock
    private ListService listServiceMock;

    private Cast cast = new Cast("Thandie Newton");
    private List<Cast> casts = new ArrayList<>();

    /**
     * Configures Mockito
     */
    @Before
    @SuppressWarnings("Duplicates")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(filmDataController, "userService", userServiceMock);
        ReflectionTestUtils.setField(filmDataController, "castService", castServiceMock);
        ReflectionTestUtils.setField(filmDataController, "filmService", filmServiceMock);
        ReflectionTestUtils.setField(filmDataController, "listService", listServiceMock);
        ReflectionTestUtils.setField(actorDataController, "userService", userServiceMock);
        ReflectionTestUtils.setField(actorDataController, "castService", castServiceMock);
        ReflectionTestUtils.setField(actorDataController, "filmService", filmServiceMock);
        ReflectionTestUtils.setField(actorDataController, "listService", listServiceMock);
    }

    /**
     * Assures no more methods of Service mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(userServiceMock);
        verifyNoMoreInteractions(castServiceMock);
        verifyNoMoreInteractions(filmServiceMock);
        verifyNoMoreInteractions(listServiceMock);
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Success() {
        casts.add(cast);
        when(castServiceMock.getAll()).thenReturn(casts);

        ResponseEntity actualRE = actorDataController.getActorsList();
        Assert.assertEquals(new ResponseEntity("[{\"id\":\"0\", \"name\":\"Thandie Newton\", \"oscar\":\"false\"}]", HttpStatus.OK), actualRE);

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Fail() {
        casts.add(cast);
        when(castServiceMock.getAll()).thenThrow(ServiceException.class);

        ResponseEntity actualRE = actorDataController.getActorsList();
        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Empty_Success() {
        when(castServiceMock.getAll()).thenReturn(casts);

        ResponseEntity actualRE = actorDataController.getActorsList();
        Assert.assertEquals(new ResponseEntity("[]", HttpStatus.OK), actualRE);

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see FilmDataController#addFilmToWatchList(javax.servlet.http.HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_Success() {
        when(listServiceMock.addToWatched(1, true, 1)).thenReturn(true);

        ResponseEntity actualRE = filmDataController.addFilmToWatchList(new MockHttpSession(), 1, true);
        Assert.assertEquals(new ResponseEntity("Film added to watch list", HttpStatus.OK), actualRE);

        verify(listServiceMock, times(1)).addToWatched(1, true, 1);
    }

    /**
     * @see FilmDataController#addFilmToWatchList(javax.servlet.http.HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_Fail() {
        when(listServiceMock.addToWatched(1, true, 1)).thenReturn(false);

        ResponseEntity actualRE = filmDataController.addFilmToWatchList(new MockHttpSession(), 1, true);
        Assert.assertEquals(new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST), actualRE);

        verify(listServiceMock, times(1)).addToWatched(1, true, 1);
    }

    /**
     * @see FilmDataController#addFilmToWatchList(javax.servlet.http.HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_Exception_Fail() {
        when(listServiceMock.addToWatched(1, true, 1)).thenThrow(ServiceException.class);

        ResponseEntity actualRE = filmDataController.addFilmToWatchList(new MockHttpSession(), 1, true);
        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);

        verify(listServiceMock, times(1)).addToWatched(1, true, 1);
    }

//    /**
//     * @see DataController#
//     */
//    @Test
//    public void addToPlanned_Success() {
//        when(listServiceMock.addToWatched(1, true, 1)).thenReturn(true);
//
//        ResponseEntity actualRE = filmDataController.addFilmToWatchList(1, 1, true);
//        Assert.assertEquals(new ResponseEntity("Film added to watch list", HttpStatus.OK), actualRE);
//
//        verify(listServiceMock, times(1)).addToWatched(1, true, 1);
//    }
//
//    /**
//     * @see DataController#
//     */
//    @Test
//    public void addToPlanned_Fail() {
//        when(listServiceMock.addToPlanned(1, true, 1)).thenReturn(false);
//
//        ResponseEntity actualRE = filmDataController.addFilmToWishList(1, 1, true);
//        Assert.assertEquals(new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST), actualRE);
//
//        verify(listServiceMock, times(1)).addToPlanned(1, true, 1);
//    }
//
//    /**
//     * @see DataController#
//     */
//    @Test
//    public void addToPlanned_Exception_Fail() {
//        when(listServiceMock.addToPlanned(1, true, 1)).thenThrow(ServiceException.class);
//
//        ResponseEntity actualRE = filmDataController.addFilmToWishList(1, 1, true);
//        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);
//
//        verify(listServiceMock, times(1)).addToPlanned(1, true, 1);
//    }

    /**
     * @see FilmDataController#removeFromWishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removePlanned_Success() {
        when(listServiceMock.removeFromPlanned(1, 1)).thenReturn(true);

        ResponseEntity actualRE = filmDataController.removeFromWishList(new MockHttpSession(), 1);
        Assert.assertEquals(new ResponseEntity("Film removed from wish list", HttpStatus.OK), actualRE);

        verify(listServiceMock, times(1)).removeFromPlanned(1, 1);
    }

    /**
     * @see FilmDataController#removeFromWishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removePlanned_Fail() {
        when(listServiceMock.removeFromPlanned(1, 1)).thenReturn(false);

        ResponseEntity actualRE = filmDataController.removeFromWishList(new MockHttpSession(), 1);
        Assert.assertEquals(new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST), actualRE);

        verify(listServiceMock, times(1)).removeFromPlanned(1, 1);
    }

    /**
     * @see FilmDataController#removeFromWishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removePlanned_Exception_Fail() {
        when(listServiceMock.removeFromPlanned(1, 1)).thenThrow(ServiceException.class);

        ResponseEntity actualRE = filmDataController.removeFromWishList(new MockHttpSession(), 1);
        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);

        verify(listServiceMock, times(1)).removeFromPlanned(1, 1);
    }

    /**
     * @see FilmDataController#removeFromWatchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removeWatched_Success() {
        when(listServiceMock.removeFromWatched(1, 1)).thenReturn(true);

        ResponseEntity actualRE = filmDataController.removeFromWatchList(new MockHttpSession(), 1);
        Assert.assertEquals(new ResponseEntity("Film removed from watch list", HttpStatus.OK), actualRE);

        verify(listServiceMock, times(1)).removeFromWatched(1, 1);
    }

    /**
     * @see FilmDataController#removeFromWatchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removeWatched_Fail() {
        when(listServiceMock.removeFromWatched(1, 1)).thenReturn(false);

        ResponseEntity actualRE = filmDataController.removeFromWatchList(new MockHttpSession(), 1);
        Assert.assertEquals(new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST), actualRE);

        verify(listServiceMock, times(1)).removeFromWatched(1, 1);
    }

    /**
     * @see FilmDataController#removeFromWatchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removeWatched_Exception_Fail() {
        when(listServiceMock.removeFromWatched(1, 1)).thenThrow(ServiceException.class);

        ResponseEntity actualRE = filmDataController.removeFromWatchList(new MockHttpSession(), 1);
        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);

        verify(listServiceMock, times(1)).removeFromWatched(1, 1);
    }
}

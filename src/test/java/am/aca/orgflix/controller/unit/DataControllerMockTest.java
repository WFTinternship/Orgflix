package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.FilmDataController;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.exception.ServiceException;
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

import static org.mockito.Mockito.*;

/**
 * Unit tests for Data Controller
 */
public class DataControllerMockTest extends BaseUnitTest {

    @Autowired
    private FilmDataController filmDataController;

    @Mock
    private FilmService filmServiceMock;
    @Mock
    private ListService listServiceMock;

    /**
     * Configures Mockito
     */
    @Before
    @SuppressWarnings("Duplicates")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(filmDataController, "filmService", filmServiceMock);
        ReflectionTestUtils.setField(filmDataController, "listService", listServiceMock);
    }

    /**
     * Assures no more methods of Service mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(filmServiceMock);
        verifyNoMoreInteractions(listServiceMock);
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

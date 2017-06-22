//package service;
//
//import am.aca.entity.Film;
//import am.aca.entity.User;
//import am.aca.service.FilmService;
//import am.aca.service.ListService;
//import am.aca.service.UserService;
//import am.aca.service.impl.FilmServiceImpl;
//import am.aca.service.impl.ListServiceImpl;
//import am.aca.service.impl.UserServiceImpl;
//import am.aca.util.DbManager;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
///**
// * Created by karine on 6/21/2017
// */
//public class ListTest {
//
//    private ListService listService = new ListServiceImpl();
//    private FilmService filmService = new FilmServiceImpl();
////    private UserService userService = new UserServiceImpl();
//    private Film film;
//    private User user;
////    private ArrayList<Cast> actors = new ArrayList<>();
//
//    @Before
//    public void setup() {
//        film = new Film("Title", 1999);
//        filmService.addFilm(film);
//
//        user = new User("hulk", "Bruce Banner", "brucebanner@avengers.com", "natasha");
//        userService.addUser(user);
//
//    }
//
//    @After
//    public void revert() {
//    }
//
//    @Test
//    public void addToWatchedWhenNotPlanned_Succeeded() {
//        listService.addToWatched(film, true, user.getId());
////        listService.
//    }
//
//    @Test
//    public void addToWatchedWhenPlanned_Succeeded() {
//        //setup List
//        listService.addToPlanned(film, true, user.getId());
//
//        listService.addToWatched(film, true, user.getId());
//        Assert.assertEquals(1, listService.showOwnWatched(user.getId()).size());
//    }
//
//    @Test
//    public void addToPlannedWhenNotWatched_Succeeded() {
//        listService.addToPlanned(film, true, user.getId());
//        Assert.assertEquals(1, listService.showOwnPlanned(user.getId()).size());
//    }
//
//    @Test
//    public void addToPlannedWhenWatchedSucceeded() {
//        //setup List
//        listService.addToWatched(film, true, user.getId());
//
//        listService.addToPlanned(film, true, user.getId());
//        Assert.assertEquals(1, listService.showOwnWatched(user.getId()).size());
//    }
//
//    @Test
//    public void removeFromPlannedWhenNotWatched_Succeeded() {
//        //setup List
//        listService.addToPlanned(film, true, user.getId());
//
//        listService.removeFromPlanned(film, user.getId());
//        Assert.assertEquals(0,listService.showOwnPlanned(user.getId()).size());
//    }
//
//    @Test
//    public void removeFromPlannedWhenWatched_Succeeded() {
//        //setup List
//        listService.addToPlanned(film, true, user.getId());
//        listService.addToWatched(film, true, user.getId());
//
//        listService.removeFromPlanned(film, user.getId());
//        Assert.assertEquals(1, listService.showOwnWatched(user.getId()).size());
//    }
//
//    @Test
//    public void removeFromWatchedWhenNotPlanned_Succeeded() {
//        //setup List
//        listService.addToWatched(film, true, user.getId());
//
//        listService.removeFromWatched(film, user.getId());
//        Assert.assertEquals(listService.showOwnWatched(user.getId()).size(), 0);
//    }
//
//    @Test
//    public void removeFromWatchedWhenPlanned_Succeeded() {
//        //setup List
//        listService.addToPlanned(film, true, user.getId());
//        listService.addToWatched(film, true, user.getId());
//
//        listService.removeFromWatched(film, user.getId());
//        Assert.assertEquals(listService.showOwnPlanned(user.getId()).size(), 1);
//    }
//
////    @Test
////    public void removeFromPlannedWhenNotPlanned_Failed() {
////        //List setup, empty list
////
////        Assert.assertFalse(listService.removeFromPlanned(film, user.getId()));
////    }
//
////    @Test
////    public void removeFromPlannedWhenWatched_Failed() {
////        //setup List
////        listService.addToWatched(film, true, user.getId());
////
////        Assert.assertFalse(listService.removeFromPlanned(film, user.getId()));
////    }
//
////    @Test
////    public void removeFromWatchedWhenNotPlanned_Failed() {
////        //List setup, empty list
////
////        Assert.assertFalse(listService.removeFromWatched(film, user.getId()));
////    }
//
////    @Test
////    public void removeFromWatchedWhenPlanned_Failed() {
////        //setup List
////        listService.addToPlanned(film, true, user.getId());
////
////        Assert.assertFalse(listService.removeFromWatched(film, user.getId()));
////    }
//
//    @Test
//    public void showOthersPlannedFailed() {
//        //setup List
//        listService.addToPlanned(film, false, user.getId());
//
//        Assert.assertEquals(0, listService.showOthersPlanned(user.getId()).size());
//    }
//
//    @Test
//    public void showOthersWatchedFailed() {
//        //setup List
//        listService.addToWatched(film, false, user.getId());
//
//        Assert.assertEquals(0, listService.showOthersWatched(user.getId()).size());
//    }
//}

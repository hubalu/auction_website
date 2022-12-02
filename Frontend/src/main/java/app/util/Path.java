package app.util;

import lombok.*;

public class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static class Web {
        @Getter public static final String INDEX = "/index/";
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String BOOKS = "/books/";
        @Getter public static final String ONE_BOOK = "/books/:isbn/";
        @Getter public static final String ITEM = "/items/";
        @Getter public static final String ITEMS = "/allitems/";
        @Getter public static final String ONE_ITEM = "/items/:ItemID/";
        @Getter public static final String UPLOAD_AUCTION = "/auction/";
        @Getter public static final String ALL_AUCTIONS = "/auctions/";
        @Getter public static final String ONE_AUCTION = "/auction/:AuctionID/";
        @Getter public static final String CREATE_USER = "/user/";
        @Getter public static final String USER_INFO = "/user_info/";
        @Getter public static final String ONE_USER_INFO = "/user_info/:userid/";
        @Getter public static final String UPDATE_PASSWORD = "/update_password/";
        @Getter public static final String UPDATE_USER_INFO = "/update_user_info/";
        @Getter public static final String SUSPEND_USER = "/suspend_user/";
        @Getter public static final String UNSUSPEND_USER = "/unsuspend_user/";
        @Getter public static final String DELETE_USER = "/delete_user/";
        @Getter public static final String UPLOAD_BID = "/upload_bid/";
        @Getter public static final String ADD_TO_WATCHLIST = "/add_to_watchlist/";
        @Getter public static final String CATEGORY = "/category/";
        @Getter public static final String DELETE_CATEGORY = "/delete_category/";
        @Getter public static final String UPDATE_CATEGORY = "/update_category/";
    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public final static String BOOKS_ALL = "/velocity/book/all.vm";
        public static final String BOOKS_ONE = "/velocity/book/one.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
        public static final String UNAUTHORIZED = "/velocity/unauthorized.vm";
        public static final String ITEM = "/velocity/item/item.vm";

        public static final String ITEMS = "/velocity/item/allitems.vm";
        public static final String ONE_ITEM = "/velocity/item/oneitem.vm";
        public static final String UPLOAD_AUCTION = "/velocity/auction/auction.vm";
        public static final String ALL_AUCTIONS = "/velocity/auction/allauctions.vm";
        public static final String ONE_AUCTION = "/velocity/auction/oneauction.vm";
        public static final String CREATE_USER = "/velocity/login/createuser.vm";

        public static final String ALL_USER = "/velocity/user/allusers.vm";
        public static final String ONE_USER = "/velocity/user/oneuser.vm";

        public static final String CATEGORY = "/velocity/category/category.vm";
    }

}

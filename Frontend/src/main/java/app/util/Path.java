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
    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public final static String BOOKS_ALL = "/velocity/book/all.vm";
        public static final String BOOKS_ONE = "/velocity/book/one.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
        public static final String ITEM = "/velocity/item/item.vm";

        public static final String ITEMS = "/velocity/item/allitems.vm";
        public static final String ONE_ITEM = "/velocity/item/oneitem.vm";
        public static final String UPLOAD_AUCTION = "/velocity/auction/auction.vm";
        public static final String ALL_AUCTIONS = "/velocity/auction/allauctions.vm";
        public static final String ONE_AUCTION = "/velocity/auction/oneauction.vm";
    }

}

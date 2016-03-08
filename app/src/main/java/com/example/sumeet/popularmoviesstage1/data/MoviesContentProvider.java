package com.example.sumeet.popularmoviesstage1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;

/**
 * Created by sumeet on 7/3/16.
 */
public class MoviesContentProvider extends ContentProvider{

    MovieDbHelper movieDbHelper;



    public static final int MOVIE_DETAILS_ITEM_LIST = 1;
    public static final int MOVIE_DETAILS_ITEM_ID = 2;

    private static final UriMatcher URI_MATCHER;

    static{

        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(MoviesContract.AUTHORITY,"movieDetails",MOVIE_DETAILS_ITEM_LIST);
        URI_MATCHER.addURI(MoviesContract.AUTHORITY,"movieDetails/#",MOVIE_DETAILS_ITEM_ID);





    }



    @Nullable
    @Override
    public String getType(Uri uri) {


        switch (URI_MATCHER.match(uri))
        {
            case MOVIE_DETAILS_ITEM_LIST:

                return MoviesContract.Movie.CONTENT_TYPE;

            case MOVIE_DETAILS_ITEM_ID:

                return MoviesContract.Movie.CONTENT_ITEM_TYPE;


            default:

                return null;
        }



    }




    @Override
    public boolean onCreate() {

        movieDbHelper = new MovieDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        Cursor cursor = null;

        switch (URI_MATCHER.match(uri))
        {
            case MOVIE_DETAILS_ITEM_LIST:

                cursor =  db.query(MoviesContract.Movie.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);


                break;

            case MOVIE_DETAILS_ITEM_ID:

                cursor = db.query(MoviesContract.Movie.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);

                break;



            default:
                break;
        }


        return cursor;
    }




    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        long id = 0;

        if(URI_MATCHER.match(uri) == MOVIE_DETAILS_ITEM_LIST)
        {
            id = db.insert(MoviesContract.Movie.TABLE_NAME,null,values);
            getContext().getContentResolver().notifyChange(uri,null);

        }




        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int delCount = db.delete(MoviesContract.Movie.TABLE_NAME,selection,selectionArgs);

        switch (URI_MATCHER.match(uri))
        {

            case MOVIE_DETAILS_ITEM_LIST:



                break;

            case MOVIE_DETAILS_ITEM_ID:




                break;

        }

        getContext().getContentResolver().notifyChange(uri,null);



        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(MoviesContract.Movie.TABLE_NAME,values,selection,selectionArgs);


        getContext().getContentResolver().notifyChange(uri,null);

        return rowsUpdated;
    }




}

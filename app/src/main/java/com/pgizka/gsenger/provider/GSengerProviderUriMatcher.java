package com.pgizka.gsenger.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.util.SparseArray;

/**
 * Provides methods to match a {@link android.net.Uri} to a {@link GSengerUriEnum}.
 * <p />
 * All methods are thread safe, except {@link #buildUriMatcher()} and {@link #buildEnumsMap()},
 * which is why they are called only from the constructor.
 */
public class GSengerProviderUriMatcher {

    /**
     * All methods on a {@link UriMatcher} are thread safe, except {@code addURI}.
     */
    private UriMatcher mUriMatcher;

    private SparseArray<GSengerUriEnum> enumsMap = new SparseArray<>();

    /**
     * This constructor needs to be called from a thread-safe method as it isn't thread-safe itself.
     */
    public GSengerProviderUriMatcher(){
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        buildUriMatcher();
    }

    private void buildUriMatcher() {
        final String authority = GSengerContract.CONTENT_AUTHORITY;

        GSengerUriEnum[] uris = GSengerUriEnum.values();
        for (int i = 0; i < uris.length; i++) {
            mUriMatcher.addURI(authority, uris[i].path, uris[i].code);
        }

        buildEnumsMap();
    }

    private void buildEnumsMap() {
        GSengerUriEnum[] uris = GSengerUriEnum.values();
        for (int i = 0; i < uris.length; i++) {
            enumsMap.put(uris[i].code, uris[i]);
        }
    }

    /**
     * Matches a {@code uri} to a {@link GSengerUriEnum}.
     *
     * @return the {@link GSengerUriEnum}, or throws new UnsupportedOperationException if no match.
     */
    public GSengerUriEnum matchUri(Uri uri){
        final int code = mUriMatcher.match(uri);
        try {
            return matchCode(code);
        } catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException("Unknown uri " + uri);
        }
    }

    /**
     * Matches a {@code code} to a {@link GSengerUriEnum}.
     *
     * @return the {@link GSengerUriEnum}, or throws new UnsupportedOperationException if no match.
     */
    public GSengerUriEnum matchCode(int code){
        GSengerUriEnum scheduleUriEnum = enumsMap.get(code);
        if (scheduleUriEnum != null){
            return scheduleUriEnum;
        } else {
            throw new UnsupportedOperationException("Unknown uri with code " + code);
        }
    }
}


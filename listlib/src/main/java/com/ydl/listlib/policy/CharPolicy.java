package com.ydl.listlib.policy;

import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ydl.listlib.interfaces.AutocompletePolicy;


public class CharPolicy implements AutocompletePolicy {

    private final static String TAG = CharPolicy.class.getSimpleName();
    private final static boolean DEBUG = false;

    private static void log(@NonNull String log) {
        if (DEBUG) Log.e(TAG, log);
    }

    private final char CH;
    private final int[] INT = new int[2];
    private boolean needSpaceBefore = true;

    public CharPolicy(char trigger) {
        CH = trigger;
    }

    public CharPolicy(char trigger, boolean needSpaceBefore) {
        CH = trigger;
        this.needSpaceBefore = needSpaceBefore;
    }

    protected boolean isValidChar(char ch) {
        return !Character.isWhitespace(ch);
    }

    @Nullable
    private int[] checkText(@NonNull Spannable text, int cursorPos) {
        final int spanEnd = cursorPos;
        char last = 'x';
        cursorPos -= 1; // If the cursor is at the end, we will have cursorPos = length. Go back by 1.
        while (cursorPos >= 0 && last != CH) {
            char ch = text.charAt(cursorPos);
            log("checkText: char is "+ch);
            if (isValidChar(ch)) {
                // We are going back
                log("checkText: char is valid");
                cursorPos -= 1;
                last = ch;
            } else {
                // We got a whitespace before getting a CH. This is invalid.
                log("checkText: char is not valid, returning NULL");
                return null;
            }
        }
        cursorPos += 1; // + 1 because we end BEHIND the valid selection

        // Start checking.
        if (cursorPos == 0 && last != CH) {
            // We got to the start of the string, and no CH was encountered. Nothing to do.
            log("checkText: got to start but no CH, returning NULL");
            return null;
        }

        // Additional checks for cursorPos - 1
        if (cursorPos > 0 && needSpaceBefore) {
            char ch = text.charAt(cursorPos-1);
            if (!Character.isWhitespace(ch)) {
                log("checkText: char before is not whitespace, returning NULL");
                return null;
            }
        }

        // All seems OK.
        final int spanStart = cursorPos + 1; // + 1 because we want to exclude CH from the query
        INT[0] = spanStart;
        INT[1] = spanEnd;
        log("checkText: found! cursorPos="+cursorPos);
        log("checkText: found! spanStart="+spanStart);
        log("checkText: found! spanEnd="+spanEnd);
        return INT;
    }

    @Override
    public boolean shouldShowPopup(@NonNull Spannable text, int cursorPos) {
        // Returning true if, right before cursorPos, we have a word starting with @.
        log("shouldShowPopup: text is "+text);
        log("shouldShowPopup: cursorPos is "+cursorPos);
        int[] show = checkText(text, cursorPos);
        if (show != null) {
            text.setSpan(new QuerySpan(), show[0], show[1], Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            return true;
        }
        log("shouldShowPopup: returning false");
        return false;
    }

    @Override
    public boolean shouldDismissPopup(@NonNull Spannable text, int cursorPos) {
        log("shouldDismissPopup: text is "+text);
        log("shouldDismissPopup: cursorPos is "+cursorPos);
        boolean dismiss = checkText(text, cursorPos) == null;
        log("shouldDismissPopup: returning "+dismiss);
        return dismiss;
    }

    @NonNull
    @Override
    public CharSequence getQuery(@NonNull Spannable text) {
        QuerySpan[] span = text.getSpans(0, text.length(), QuerySpan.class);
        if (span == null || span.length == 0) {
            // Should never happen.
            log("getQuery: there's no span!");
            return "";
        }
        log("getQuery: found spans: "+span.length);
        QuerySpan sp = span[0];
        log("getQuery: span start is "+text.getSpanStart(sp));
        log("getQuery: span end is "+text.getSpanEnd(sp));
        CharSequence seq =  text.subSequence(text.getSpanStart(sp), text.getSpanEnd(sp));
        log("getQuery: returning "+seq);
        return seq;
    }


    @Override
    public void onDismiss(@NonNull Spannable text) {
        // Remove any span added by shouldShow. Should be useless, but anyway.
        QuerySpan[] span = text.getSpans(0, text.length(), QuerySpan.class);
        for (QuerySpan s : span) {
            text.removeSpan(s);
        }
    }

    private static class QuerySpan {}

    @Nullable
    public static int[] getQueryRange(@NonNull Spannable text) {
        QuerySpan[] span = text.getSpans(0, text.length(), QuerySpan.class);
        if (span == null || span.length == 0) return null;
        if (span.length > 1) {
            // Won't happen
            log("getQueryRange:  ERR: MORE THAN ONE QuerySpan.");
        }
        QuerySpan sp = span[0];
        return new int[]{text.getSpanStart(sp), text.getSpanEnd(sp)};
    }
}

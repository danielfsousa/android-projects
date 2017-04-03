package com.danisousa.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.danisousa.criminalintent.Crime;
import com.danisousa.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {

    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String suspectName = getString(getColumnIndex(CrimeTable.Cols.SUSPECT_NAME));
        String suspectPhone = getString(getColumnIndex(CrimeTable.Cols.SUSPECT_PHONE));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSuspectName(suspectName);
        crime.setSuspectPhone(suspectPhone);

        return crime;
    }

}

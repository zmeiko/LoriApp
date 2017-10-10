package com.ikvant.loriapp.state.timeentry;

import android.util.Log;

import com.ikvant.loriapp.database.timeentry.TimeEntry;
import com.ikvant.loriapp.database.timeentry.TimeEntryDao;
import com.ikvant.loriapp.network.LoriApiService;
import com.ikvant.loriapp.network.NetworkApiException;
import com.ikvant.loriapp.state.auth.AuthController;
import com.ikvant.loriapp.utils.AppExecutors;
import com.ikvant.loriapp.utils.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ikvant.
 */

public class LoriTimeEntryController implements TimeEntryController {
    private static final String TAG = "LoriTimeEntryController";

    private TimeEntryDao timeEntryDao;
    private LoriApiService apiService;
    private AppExecutors executors;
    private AuthController authController;

    public LoriTimeEntryController(TimeEntryDao timeEntryDao, LoriApiService apiService, AppExecutors executors, AuthController authController) {
        this.timeEntryDao = timeEntryDao;
        this.apiService = apiService;
        this.executors = executors;
        this.authController = authController;
    }

    @Override
    public boolean needSync() {
        return false;
    }

    @Override
    public void loadEntries(final Callback<List<TimeEntry>> callback) {
        final List<TimeEntry> result = new ArrayList<>();
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                result.addAll(timeEntryDao.load());
                executors.networkIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<TimeEntry> entryList = null;
                        try {
                            entryList = apiService.getTimeEntries();
                            if (entryList != null) {
                                result.addAll(entryList);
                            }
                            executors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(result);
                                }
                            });
                        } catch (NetworkApiException e) {
                            Log.d(TAG, "onFailure() called with: call = [" + e + "]");
                            executors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(result);
                                }
                            });
                        }

                    }
                });

            }
        });
    }
}

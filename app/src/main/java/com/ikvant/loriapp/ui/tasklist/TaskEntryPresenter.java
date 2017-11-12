package com.ikvant.loriapp.ui.tasklist;

import android.util.SparseArray;

import com.ikvant.loriapp.database.timeentry.TimeEntry;
import com.ikvant.loriapp.state.SyncController;
import com.ikvant.loriapp.state.entry.EntryController;
import com.ikvant.loriapp.state.entry.LoadDataCallback;
import com.ikvant.loriapp.state.entry.Reloadable;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by ikvant.
 */
@Singleton
public class TaskEntryPresenter implements Contract.Presenter {
    private static final String TAG = "TaskEntryPresenter";

    private Contract.View view;

    private EntryController entryController;
    private SyncController syncController;

    private boolean isFirstLoad = true;
    private boolean isActive;

    @Inject
    public TaskEntryPresenter(EntryController entryController, SyncController syncController) {
        this.entryController = entryController;
        this.syncController = syncController;
    }

    public void setView(Contract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void openEntryDetail(String id) {
        view.showEditEntryScreen(id);
    }

    @Override
    public void onResume() {
        isActive = true;
        reload(isFirstLoad, false);
    }

    @Override
    public void onPause() {
        isActive = false;
    }

    @Override
    public void createNewEntry() {
        view.showNewEntryScreen();
    }

    @Override
    public void searchEntries() {
        view.showSearchScreen();
    }

    @Override
    public void reload() {
        reload(isFirstLoad, true);
    }

    private void reload(boolean force, boolean byUser) {
        isFirstLoad = false;
        if (byUser) {
            view.showLoadingIndicator(true);
        }
        if (force) {
            syncController.sync(new Reloadable.Callback() {
                @Override
                public void onSuccess() {
                    loadTimeEntries();
                }

                @Override
                public void onOffline() {
                    loadTimeEntries();
                }

                @Override
                public void onFailure(Throwable e) {
                    if (isActive) {
                        view.showLoadingIndicator(false);
                        view.showErrorMessage(e.getMessage());
                    }
                }
            });
        } else {
            loadTimeEntries();
        }

    }

    private void loadTimeEntries() {
        entryController.loadTimeEntries(new LoadDataCallback<SparseArray<Set<TimeEntry>>>() {
            @Override
            public void onSuccess(SparseArray<Set<TimeEntry>> data) {
                if (isActive) {
                    view.showLoadingIndicator(false);
                    view.showTimeEntries(data);
                }
            }

            @Override
            public void networkUnreachable(SparseArray<Set<TimeEntry>> localData) {
                if (isActive) {
                    view.showLoadingIndicator(false);
                    view.showTimeEntries(localData);
                    view.showOfflineMessage();
                }
            }

            @Override
            public void onFailure(Throwable e) {
                if (isActive) {
                    view.showLoadingIndicator(false);
                    view.showErrorMessage(e.getMessage());
                }
            }
        });
    }


}

package com.ikvant.loriapp.ui.editenrty;

import android.support.annotation.StringRes;

import com.ikvant.loriapp.database.project.Project;
import com.ikvant.loriapp.database.task.Task;

import java.util.Date;
import java.util.List;

/**
 * Created by ikvant.
 */

public interface Contract {
    interface View {
        void showLoadingIndicator(boolean show);

        void showCreateButton();

        void showModifyButton();

        void showDeleteButton();

        void setTasks(List<Task> tasks);

        void setProjects(List<Project> projects);

        void setPresenter(Presenter presenter);

        void setDate(Date data);

        void setTime(int time);

        void setDescription(String text);

        void setTask(int position);

        void showTimePicker(int hour, int minute);

        void showDatePicker(int day, int month, int year);

        void showErrorMessage(String text);

        void showErrorMessage(@StringRes int id);

        void showOfflineMessage();

        void finish();

        void setProject(int position);

        void setTags(String[] nameTags, boolean[] selectedTags, List<String> selectedListTags);

        void setResultCreated(String id);

        void setResultDeleted(String id);

        void setResultChanged(String id);

        void setEnabledSpinners(boolean enable);
    }

    interface Presenter {
        void saveEntry();

        void deleteEntry();

        void navigateBack();

        void setProject(int position);

        void setTask(int task);

        void setDescription(String text);

        void setDate(int dayOfMonth, int month, int year);

        void setTime(int hourOfDay, int minute);

        void openTimePickerDialog();

        void openDataPickerDialog();

        void onStart();

        void onPause();

        void checkTag(int index, boolean checked);

        void saveTags();

    }

}

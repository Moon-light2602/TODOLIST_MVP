package edu.hanu.todolist_mvp.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;
import java.util.UUID;
public class Task {
    private final String id;
    @Nullable
    private String title;
    @Nullable
    private String desc;
    private boolean completed;

    /**
     * Use this constructor to create a new active task
     * @param title
     * @param desc
     */

    public Task(@Nullable String title, @Nullable String desc) {
        id = UUID.randomUUID().toString();
        this.title = title;
        this.desc = desc;
        this.completed = false;
    }

    /**
     * Use this constructor to create an active Task if the Task already has an id
     * @param id
     * @param title
     * @param desc
     */
    public Task(String id, @Nullable String title, @Nullable String desc) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.completed = false;
    }

    /**
     * Use this constructor to create a new completed Task
     * @param title
     * @param desc
     * @param completed
     */

    public Task(@Nullable String title, @Nullable String desc, boolean completed) {
        id = UUID.randomUUID().toString();
        this.title = title;
        this.desc = desc;
        this.completed = completed;
    }

    /**
     * Use this constructor to specify a completed Task if the Task already has an id
     * @param id
     * @param title
     * @param desc
     * @param completed
     */
    public Task(String id, @Nullable String title, @Nullable String desc, boolean completed) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.completed = completed;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getTitleForList() {
        if (title != null && !title.equals("")) {
            return title;
        } else {
            return desc;
        }
    }

    @Nullable
    public String getDesc() {
        return desc;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isActive() {
        return !completed;
    }

    public boolean isEmpty() {
        return (title == null || "".equals(title)) &&
                (desc == null || "".equals(desc));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.getId()) &&
                Objects.equals(title, task.getTitle()) &&
                Objects.equals(desc, task.getDesc());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDesc());
    }

    @Override
    public String toString() {
        return "Task with title" + title;
    }
}
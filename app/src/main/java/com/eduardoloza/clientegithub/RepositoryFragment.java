package com.eduardoloza.clientegithub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import models.Repositories;

public class RepositoryFragment extends Fragment{
    private TextView name, description;
    private Repositories repositories;
    public RepositoryFragment(Repositories repositories) {
        this.repositories = repositories;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.repo_item, container, false);
        name = view.findViewById(R.id.name);
        description = view.findViewById(R.id.description);

        name.setText(this.repositories.getName());
        description.setText(this.repositories.getDescription());

        return view;
    }
}

package com.example.findu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddPost extends AppCompatDialogFragment {
    private EditText editText_name;
    private EditText editText_age;
    private EditText editText_notes;
    private Spinner spinner_gender;
    private PostDialogListener postDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.post_dialog, null);
        editText_name = view.findViewById(R.id.edittext_name);
        editText_age = view.findViewById(R.id.edittext_age);
        editText_notes = view.findViewById(R.id.edittext_notes);
        spinner_gender = view.findViewById(R.id.spinner_gender);
        builder.setView(view).setTitle("Add a post")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editText_name.getText().toString();
                        int age = Integer.parseInt(editText_age.getText().toString());
                        String note = editText_notes.getText().toString();
                        // TODO: future change for firebase
                        postDialogListener.applyTexts(name, age, note);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            postDialogListener = (PostDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "not valid postDialogListener.");
        }
    }

    public interface PostDialogListener {
        void applyTexts(String name, int age, String notes);
    }
}

package com.rightdecisions.diagonapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

/**
 * Created by Tute on 22/9/2016.
 */
public class SimpleDialogAgReco extends DialogFragment {

    OnSimpleDialogListener listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnSimpleDialogListener) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " no implementó OnSimpleDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    /**
     * Crea un diálogo de alerta sencillo
     * @return Nuevo diálogo
     */
    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText t = new EditText(getContext());

        builder.setTitle("Nuevo recorrido:")

                .setMessage("Ingrese el nombre del nuevo recorrido:")
                .setView(t)


                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                listener.onPossitiveButtonClick(String.valueOf(t.getText()));
                                dismiss();

                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onNegativeButtonClick();
                                dismiss();
                            }
                        });

        return builder.create();
    }

    public interface OnSimpleDialogListener {
        void onPossitiveButtonClick(String s);// Eventos Botón Positivo
        void onNegativeButtonClick();// Eventos Botón Negativo
    }




}
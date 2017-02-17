package com.rightdecisions.diagonapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

/**
 * Created by Tute on 22/9/2016.
 */
public class SimpleDialogDelReco extends DialogFragment {

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

        builder.setTitle("Eliminar recorrido:")

                .setMessage("¿Esta seguro de eliminar el recorrido?")

                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                listener.onPossitiveButtonClickDel();
                                dismiss();

                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onNegativeButtonClickDel();
                                dismiss();
                            }
                        });

        return builder.create();
    }

    public interface OnSimpleDialogListener {
        void onPossitiveButtonClickDel();// Eventos Botón Positivo
        void onNegativeButtonClickDel();// Eventos Botón Negativo
    }




}
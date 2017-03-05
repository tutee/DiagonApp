package com.rightdecisions.diagonapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Tute on 22/9/2016.
 */
public class SimpleDialog extends DialogFragment {

    OnSimpleDialogListener listener;
    String estado = "Origen";


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



        final CharSequence[] items = new CharSequence[2];


        items[0] = "Origen";
        items[1] = "Destino";




        builder.setTitle("Definir como:")

                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (items[which].equals("Origen") || items[which].equals("null")){
                            estado = "Origen";
                        }
                        else {
                            estado = "Destino";
                        }
                    }
                })

                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                listener.onPossitiveButtonClick(estado);
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
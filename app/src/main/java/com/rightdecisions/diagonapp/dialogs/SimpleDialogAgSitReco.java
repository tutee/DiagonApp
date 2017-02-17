package com.rightdecisions.diagonapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Tute on 22/9/2016.
 */
public class SimpleDialogAgSitReco extends DialogFragment {

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

        final ArrayList itemsSeleccionados = new ArrayList();

        final CharSequence[] items = new CharSequence[3];

        items[0] = "Soltero/a";
        items[1] = "Casado/a";
        items[2] = "Divorciado/a";

        builder.setTitle("Nuevo recorrido:")

                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            // Guardar indice seleccionado
                            itemsSeleccionados.add(which);
                            Toast.makeText(
                                    getActivity(),
                                    "Checks seleccionados:(" + itemsSeleccionados.size() + ")",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        } else if (itemsSeleccionados.contains(which)) {
                            // Remover indice sin selección
                            itemsSeleccionados.remove(Integer.valueOf(which));
                        }
                    }
                })

                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                listener.onPossitiveButtonClick(String.valueOf(String.valueOf(itemsSeleccionados)));
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
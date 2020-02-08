package com.gonzaloandcompany.woldquiz.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.gonzaloandcompany.woldquiz.PaisFragmentList;
import com.gonzaloandcompany.woldquiz.R;
import com.gonzaloandcompany.woldquiz.models.Pais;
import com.gonzaloandcompany.woldquiz.service.PaisService;
import com.gonzaloandcompany.woldquiz.service.ServiceGeneratorPais;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CurrencyFilterDialogFragment extends DialogFragment {

    View v;
    ListView lvFiltro;
    PaisService paisService;
    List<Pais> paises = new ArrayList<>();
    List<String> listaMostrar = new ArrayList<>();
    DialogPassData dialogPassData;

    public CurrencyFilterDialogFragment(DialogPassData dialogPassData, List<String> monedas) {
        this.dialogPassData = dialogPassData;
        this.listaMostrar = monedas;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Configura el dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Configuración del diálogo

        builder.setTitle("Filtro de países");
        builder.setMessage("Selecciona una moneda");

        // Hacer que el diálogo sólo se pueda cerrar desde el botón
        // Cancelar o Guardar
        builder.setCancelable(true);

        // Cargar el layout del formulario
        v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_filtro_moneda, null);
        builder.setView(v);

        lvFiltro = v.findViewById(R.id.lvFiltro);

        PaisFilterAdapter adapter = new PaisFilterAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                listaMostrar
        );


        lvFiltro.setAdapter(adapter);


        builder.setNegativeButton(R.string.button_cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancelar diálogo
                dialog.dismiss();
            }
        });
         Dialog dialog = builder.create();
        lvFiltro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)lvFiltro.getItemAtPosition(position);
                Log.d("ITEM", item);
                dialogPassData = (DialogPassData)getTargetFragment();
                dialogPassData.filterByCoin(item);
                dialog.dismiss();
            }

        });

                // Create the AlertDialog object and return it

        return dialog;
    }

}

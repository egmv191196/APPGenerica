package com.example.appbase.utils.base;

import androidx.fragment.app.DialogFragment;

import com.example.appbase.R;

public class BaseDialogFragment extends DialogFragment {
    protected void showAlert(String titulo, String mensaje) {
        showAlert(titulo,mensaje,getString(R.string.aceptar), "", () -> {}, null);
    }

    protected void showAlert(String title, String mensaje, String textoPositivo, MostrarAlertaClickInterface.positive positive) {
        showAlert(title, mensaje, textoPositivo, "", positive, null);
    }

    protected void showAlert(String titulo, String mensaje, String textoPositivo, String textoNegative, MostrarAlertaClickInterface.positive positive, MostrarAlertaClickInterface.negative negative) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).showAlert(titulo, mensaje, textoPositivo, textoNegative, positive, negative);
    }

    protected void showLoader(Boolean show) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).showLoader(show);
    }

    protected void inabilitar(Boolean show) {
        if (getActivity() != null)
            ((BaseActivity) getActivity()).inhabilitarLogin = show;
    }
}

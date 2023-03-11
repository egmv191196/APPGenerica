package com.example.appbase.utils.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appbase.R;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    public static boolean autoBloqueoPorLogin = false;
    public CompositeDisposable compositeDisposable;
    private ProgressDialog pDialog;
    public static final int NO_FLAGS = -1;
    public boolean inhabilitarLogin = true;
    public boolean gcrEnGestion = false;
    public boolean isDialog = false;
    // Variable foto
    private String pathFoto;
    private String nameFoto;
    private ImageView iView;
    private int tipoFoto;
    public boolean orientacionAutomatica = true;
    private InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        if (orientacionAutomatica) {
            if (isPhone())
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            else
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    }

    protected void showAlert(String titulo, String mensaje) {
        showAlert(titulo, mensaje, getString(R.string.aceptar), "", () -> {}, null);
    }

    protected void showAlert(String title, String mensaje, String textoPositivo, MostrarAlertaClickInterface.positive positive) {
        showAlert(title, mensaje, textoPositivo, "", positive, null);
    }

    protected void showAlert(String titulo, String mensaje, String textoPositivo, String textoNegative, MostrarAlertaClickInterface.positive positive, MostrarAlertaClickInterface.negative negative) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setCancelable(false);
        if (!textoPositivo.isEmpty()) {
            builder.setPositiveButton(textoPositivo, (dialog, which) -> {
                if (positive != null) {
                    positive.click();
                }
                dialog.dismiss();
            });
        }
        if (!textoNegative.isEmpty()) {
            builder.setNegativeButton(textoNegative, (dialog, which) -> {
                if (negative != null) {
                    negative.click();
                }
                dialog.dismiss();
            });
        }
        builder.show();
    }

    public void showLoader(Boolean show) {
        showLoader(show, getString(R.string.consultando_informacion));
    }

    public void showLoader(Boolean show, String message) {
        if (show) {
            if (pDialog == null) {
                pDialog = new ProgressDialog(this);
            }
            pDialog.setTitle(getString(R.string.atencion));
            pDialog.setMessage(message);
            pDialog.setCancelable(false);
            pDialog.show();
        } else if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    public void showDialogFragmentFullScreen(BaseDialogFragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();
    }

    protected void showDialogFragment(BaseDialogFragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, fragment).addToBackStack(addToBackStack ? fragment.getClass().getSimpleName() : null).commit();
    }

    public <T extends Fragment> void mostrarFragment(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack) {
        mostrarFragment(fragmentClass, containerViewId, bundle, addToBackStack, false);
    }

    public <T extends Fragment> void mostrarFragment(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack, boolean clearStack) {
        mostrarFragment(fragmentClass, containerViewId, bundle, addToBackStack, clearStack, false, true);
    }

    public <T extends Fragment> void mostrarFragment(Class<T> fragmentClass, int containerViewId, Bundle bundle, boolean addToBackStack, boolean clearStack, boolean commitAllowingStateLoss, boolean searchInBack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (clearStack) {
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                fragmentManager.popBackStack();
            }
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        if (searchInBack) {
            fragment = fragmentManager.findFragmentByTag(fragmentClass.getSimpleName());
        }
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
                fragment.setArguments(bundle);
            } catch (Exception e) {
                throw new RuntimeException("New Fragment should have been created", e);
            }
        }
        fragmentTransaction.replace(containerViewId, fragment, fragmentClass.getSimpleName());
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragmentClass.getName());
        }
        if (commitAllowingStateLoss) {
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            fragmentTransaction.commit();
        }
    }

    public void lanzarActivity(Intent intent, int flags) {
        inhabilitarLogin = true;
        if (flags == NO_FLAGS) {
            startActivity(intent);
        } else {
            startActivity(intent.addFlags(flags));
        }
    }

    public void lanzarActivityForResult(Intent intent, int requestCode, int flags) {
        inhabilitarLogin = true;
        if (flags == NO_FLAGS) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivityForResult(intent.addFlags(flags), requestCode);
        }
    }


    protected boolean isPhone() {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL;
    }

    public void esconderTeclado(View view) {
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

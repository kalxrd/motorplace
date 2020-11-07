package com.example.motorplace.fragments.adm

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.motorplace.R
import com.example.motorplace.activitys.TelaDeEntradaActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_perfil_adm.view.*

class PerfilAdmFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_perfil_adm, container, false)

        view.btn_sair.setOnClickListener {
            val pd = ProgressDialog(context)
            pd.setMessage("Saindo ...")
            pd.show()
            val user = FirebaseAuth.getInstance()
            user.signOut()
            val intent = Intent(context, TelaDeEntradaActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }

        return view
    }
}
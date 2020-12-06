package com.example.motorplace.fragments.cliente


import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.motorplace.R
import com.example.motorplace.activitys.FavoritosActivity
import com.example.motorplace.adapter.ServicosAdapter
import com.example.motorplace.model.Servico
import com.example.motorplace.util.Permissao
import com.google.firebase.database.*
import com.jama.carouselview.CarouselView
//import com.synnapps.carouselview.CarouselView
//import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var recyclerViewServicos: RecyclerView
    private var servicos = arrayListOf<Servico>()
    private lateinit var adapterServico: ServicosAdapter
    private lateinit var servicosRecuperados : DatabaseReference

    private val permisssaoLigacao= arrayOf(Manifest.permission.CALL_PHONE)
    private val SELECAO_PHONE = 100

    private val images = arrayListOf( R.drawable.desconto_oleo,
        R.drawable.desconto_pneu,
        R.drawable.dinheiro
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View =  inflater.inflate(R.layout.fragment_home, container, false)

        view.btn_guincho.setOnClickListener {
            confirmarLigacao()
        }

        view.btn_wpp.setOnClickListener {
            abrirWpp()
        }

        view.btn_favorito.setOnClickListener {
            startActivity(Intent(context,FavoritosActivity::class.java))
        }


        val carouselView = view.findViewById<CarouselView>(R.id.carouselView)

        carouselView.apply {
            resource = R.layout.image_carousel_item
            setCarouselViewListener { view, position ->
                // Example here is setting up a full image carousel
                val imageView = view.findViewById<ImageView>(R.id.imagema)
                imageView.setImageDrawable(resources.getDrawable(images[position]))
            }
            // After you finish setting up, show the CarouselView
            show()
        }

        servicosRecuperados =  FirebaseDatabase.getInstance().reference.child("servicos")

        recyclerViewServicos = view.findViewById(R.id.recycler_promocoes_feed)
        recyclerViewServicos.layoutManager =  StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewServicos.hasFixedSize()

        adapterServico = ServicosAdapter(view.context!!,servicos)

        recyclerViewServicos.adapter = adapterServico

        //recupera dados
        recuperarServico()



        return view
    }


    private fun recuperarServico(){
        servicosRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                servicos.clear()
                for (d in dataSnapshot.children){
                    var u = d.getValue(Servico::class.java)
                    if(!u!!.prazo.isEmpty()){
                        servicos.add(u!!)
                    }

                }

                Collections.reverse(servicos)
                adapterServico.notifyDataSetChanged()
            }

        })
    }

    private fun abrirWpp(){
        var url = "https:/api.whatsapp.com/send?phone=559288540875"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    }

    private fun ligarGuincho(){
        val uri  = Uri.parse("tel:988540875")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        startActivity(intent)
    }

    fun confirmarLigacao(){
        if (Permissao.validarPermissao(permisssaoLigacao, activity, SELECAO_PHONE)) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Ligar para Guincho")
            builder.setMessage("Você tem certeza que deseja LIGAR para o guincho?")
            builder.setCancelable(false)

            builder.setPositiveButton("Ligar"){ dialogInterface, i ->ligarGuincho() }

            builder.setNegativeButton("Cancelar"){ dialogInterface, i ->}

            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissaoResultado in grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissao()

            }else if (requestCode == SELECAO_PHONE) { //se foi aceita a permissao ira abrir a opcao da camera ou galeria
                val uri  = Uri.parse("tel:92988540875")
                val intent = Intent(Intent.ACTION_DIAL, uri)
                startActivity(intent)
            }
        }
    }


    fun alertaPermissao(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Permissão Negada")
        builder.setMessage("Para poder acessar este recurso é neceessário aceitar a permissão")
        builder.setCancelable(false)
        builder.setPositiveButton("OK"){ dialogInterface, i ->  }


        val dialog = builder.create()
        dialog.show()
    }

}
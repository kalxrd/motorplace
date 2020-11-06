package com.example.motorplace.activitys

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.motorplace.R
import com.example.motorplace.model.Carro
import com.example.motorplace.util.MyMaskEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_atualizar_carro.*


class AtualizarCarroActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var placa: TextView
    private lateinit var modelo: TextView
    private lateinit var marca: TextView
    private lateinit var ano: TextView
    private lateinit var cor: TextView
    private  var tamanho : String = ""
    private lateinit var pd  : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atualizar_carro)
        supportActionBar!!.title ="Atualizar dados veículo"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pd = ProgressDialog(this)
        pd.setMessage("Recuperando Dados ...")
        pd.show()

        inicializar()
        carregarDados()
        
        btn_cancelar_carro.setOnClickListener { 
            finish()
        }
        
        btn_salvar_carrro.setOnClickListener { 
            salvarDados()
        }
    }

    private fun inicializar(){
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        placa = findViewById(R.id.txt_placa_carro)
        modelo = findViewById(R.id.txt_modelo_carro)
        marca = findViewById(R.id.txt_marca_carro)
        ano = findViewById(R.id.txt_ano_carro)
        cor = findViewById(R.id.txt_cor_carro)

        txt_ano_carro.myCustomMask("####")

        //preenche os dados do spinner
        val spinnerTamanho: Spinner = findViewById(R.id.spinner_tamanho_carro)
        ArrayAdapter.createFromResource(
            this, R.array.tamanho, android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerTamanho.adapter = adapter
        }

        //verifica o spinner selecionado
        spinnerTamanho.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //(parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#bdbdbd"))
                (parent.getChildAt(0) as TextView).setTypeface(Typeface.DEFAULT)
                tamanho = spinnerTamanho.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        })
    }

    private fun carregarDados(){
        val user =auth.currentUser
        val carro = database.child("carros").child(auth.uid!!)


        user?.let{
            carro.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var car = dataSnapshot.getValue(Carro::class.java)!!

                    placa.text = car.placa
                    modelo.text = car.modelo
                    marca.text = car.marca
                    ano.text = car.ano
                    cor.text = car.cor

                    if(car.tamanho.equals("Passeio/Leve")){
                        spinner_tamanho_carro.setSelection(1)
                    }else if(car.tamanho.equals("Pickup")){
                        spinner_tamanho_carro.setSelection(2)
                    }else if(car.tamanho.equals("Van")){
                        spinner_tamanho_carro.setSelection(3)
                    }
                    
                    pd.dismiss()
                }

            })
        }
    }

    private fun salvarDados(){
        val pd = ProgressDialog(this)
        pd.setMessage("Salvando...")
        pd.show()

        val user =auth.currentUser!!
        val carro = Carro()



        carro.placa = placa.text.toString()
        carro.modelo = modelo.text.toString()
        carro.marca = marca.text.toString()
        carro.ano = ano.text.toString()
        carro.cor = cor.text.toString()
        carro.tamanho = tamanho
        carro.idUsuario = user.uid

        database.child("carros").child(user.uid!!).setValue(carro).addOnCompleteListener {
            if(it.isSuccessful){
                val intent = Intent(this,TelaHomeCliente::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent)
            }

        }
    }
    
    //metodo de acesso a classe da maskara
    fun EditText.myCustomMask(mask: String) {
        addTextChangedListener(MyMaskEditText(this, mask))}
}
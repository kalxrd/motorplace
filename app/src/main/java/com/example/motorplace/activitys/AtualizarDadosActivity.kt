package com.example.motorplace.activitys

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.motorplace.R
import com.example.motorplace.model.Usuario
import com.example.motorplace.util.MyMaskEditText
import com.example.motorplace.util.Permissao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_atualizar_dados.*
import java.io.ByteArrayOutputStream

class AtualizarDadosActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var nome: TextView
    private lateinit var cpf: TextView
    private lateinit var telefone: TextView
    private lateinit var email: TextView
    private lateinit var data: TextView
    private lateinit var imagemPerfil: CircleImageView
    private val permisssaoCamera= arrayOf(Manifest.permission.CAMERA) //array com as permições que o app precisará (camera)
    private val permisssaoGaleria = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE) //array com as permições que o app precisará (Galeria)
    private val SELECAO_CAMERA = 100
    private val SELECAO_GALERIA = 200
    private var imagem: Bitmap? = null
    private lateinit var storageReference : StorageReference
    private lateinit var u: Usuario
   private lateinit var progressImage: ProgressBar
    private lateinit var pd  : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atualizar_dados)
        supportActionBar!!.title ="Atualizar dados pessoais"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        pd = ProgressDialog(this)
        pd.setMessage("Recuperando Dados ...")
        pd.show()

        inicializar()
        carregarDados()

        alterar_imagem.setOnClickListener {
            mudarFoto()
        }
        btn_cancelar.setOnClickListener {
            finish()
        }
        btn_salvar.setOnClickListener {
            editar()
        }
    }

    private fun inicializar(){
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        imagemPerfil = findViewById(R.id.imagem_perfil_editar)
        storageReference = FirebaseStorage.getInstance().reference
        progressImage = findViewById(R.id.progressImage)

        nome = findViewById(R.id.txt_nome)
        email = findViewById(R.id.txt_email)
        cpf = findViewById(R.id.txt_cpf)
        telefone = findViewById(R.id.txt_telefone)
        data = findViewById(R.id.txt_data)

        //coloca a mascara nos editTexts
        txt_data.myCustomMask("##/##/####")
        txt_telefone.myCustomMask("(##) #####-####")
        txt_cpf.myCustomMask("###.###.###-##")
    }

    private fun carregarDados(){
        val user =auth.currentUser
        val usuario = database.child("usuarios").child(auth.uid!!)


        user?.let{
            usuario.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    u = dataSnapshot.getValue(Usuario::class.java)!!

                    if(!u.foto.isEmpty()){
                        Picasso.get()
                            .load(u?.foto)
                            .into(imagemPerfil)
                        progressImage.visibility = View.GONE
                    }else{
                        progressImage.visibility = View.GONE
                    }

                    nome.text = u.nome
                    email.text = u.email
                    cpf.text = u.cpf
                    telefone.text = u.telefone
                    data.text = u.dataNasc

                    pd.dismiss()
                }

            })
        }
    }
    private fun editar(){
        val pd = ProgressDialog(this)
        pd.setMessage("Salvando...")
        pd.show()

        val user =auth.currentUser!!
        val usuario = Usuario()

        usuario.nome = nome.text.toString()
        usuario.email =email.text.toString()
        usuario.foto = u.foto
        usuario.cpf = cpf.text.toString()
        usuario.telefone = telefone.text.toString()
        usuario.dataNasc = data.text.toString()

        database.child("usuarios").child(user.uid!!).setValue(usuario)
        if (imagem!=null){
            salvarFoto(database.child("usuarios").child(user.uid))
        }else{
            val intent = Intent(this,TelaHomeCliente::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }
    }


    fun mudarFoto() {
        val modes = arrayOf("Tirar foto da câmera", "Selecionar foto da galeria")

        //Cria uma AlertDialog
        val builder = AlertDialog.Builder(this)

        //Seta o título
        builder.setTitle("Selecione uma opção") //Seta os itens de opção
            .setItems(modes) { dialogInterface, i ->

                //Verifica o índice do item
                when (i) {
                    0 -> //abre as permissoes para camera/ se o usuario tiver permissao irá abrir a camera
                        if (Permissao.validarPermissao(permisssaoCamera, this, SELECAO_CAMERA)) {
                            if (baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                if (intent.resolveActivity(packageManager) != null) {
                                    startActivityForResult(intent, SELECAO_CAMERA)
                                }
                            } else {
                                //caso não tenha acesso aos recursos da camera
                            }
                        }
                    1 -> {
                        //abre as permissoes para galeria/ se o usuario tiver permissao irá abrir a galeria
                        if (Permissao.validarPermissao(permisssaoGaleria, this, SELECAO_GALERIA)) {
                            val intent =
                                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivityForResult(intent, SELECAO_GALERIA)
                            }
                        }
                    }
                }
            }
            .create()
            .show()
    }


    //metodo de acesso a classe da maskara
    fun EditText.myCustomMask(mask: String) {
        addTextChangedListener(MyMaskEditText(this, mask))}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permissaoResultado in grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaPermissao()

            }else if (requestCode == SELECAO_GALERIA) { //se foi aceita a permissao ira abrir a opcao da camera ou galeria
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA)
                }

            } else if (requestCode == SELECAO_CAMERA) {
                if (baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivityForResult(intent, SELECAO_CAMERA)
                    }
                } else {
                    //notFound
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            try {
                when (requestCode) {
                    SELECAO_CAMERA -> {
                        imagem = data?.extras?.get("data") as Bitmap
                    }
                    SELECAO_GALERIA -> {
                        val localImagemSelecionada = data?.data
                        imagem = MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            localImagemSelecionada
                        )
                    }
                }
                if(imagem != null){
                    //recuperar dados da imagem para o firebase
                    imagemPerfil.setImageBitmap(imagem)
                }
            } catch (e:java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun alertaPermissao(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissão Negada")
        builder.setMessage("Para poder acessar este recurso é neceessário aceitar a permissão")
        builder.setCancelable(false)
        builder.setPositiveButton("OK" ){ dialogInterface, i ->  }


        val dialog = builder.create()
        dialog.show()
    }
    fun salvarFoto(ref:DatabaseReference){
        //recuperar dados da imagem para o firebase
        val baos =  ByteArrayOutputStream()

        imagem?.compress(Bitmap.CompressFormat.JPEG, 70, baos)

        val dadosImagem = baos.toByteArray()

        //Salvar no Firebase
        val imagemRef = storageReference
            .child("imagens")
            .child("perfil")
            .child(auth!!.currentUser?.uid.toString())
            .child("perfil.jpeg")

        val uploadTask = imagemRef.putBytes(dadosImagem)
        uploadTask.addOnFailureListener{
            //Se houve erro no upload da imageFile
            Toast.makeText(this, "Erro ao salvar  a foto", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            imagemRef.downloadUrl.addOnSuccessListener {
                ref.child("foto").setValue(it.toString())
            }
            //Se o upload da imageFile foi realizado com sucesso
            Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this,TelaHomeCliente::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }
    }
}
package com.example.motorplace.activitys

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.adapter.CarrinhoAdapter
import com.example.motorplace.model.Compras
import com.example.motorplace.model.Produto
import com.example.motorplace.util.MoneyTextWatcher
import com.example.motorplace.util.carroAtual
import com.example.motorplace.util.userAtual
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_carrinho.*
import java.io.BufferedOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

class CarrinhoActivity : AppCompatActivity() {
    private lateinit var recyclerViewServicos: RecyclerView
    private var produtos = arrayListOf<Produto>()
    private lateinit var adapterProduto: CarrinhoAdapter
    private lateinit var produtosRecuperados : DatabaseReference
    private lateinit var carrinhoRecuperados : DatabaseReference
    private lateinit var database: DatabaseReference
    private var soma = 0.00
    private lateinit var valor: TextView
    private var somaCusto = 0.00
    private val CREATEPDF = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)

        supportActionBar!!.title = "Carrinho"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        valor = findViewById(R.id.valor_total)
        produtosRecuperados =  FirebaseDatabase.getInstance().reference.child("produtos")
        carrinhoRecuperados =  FirebaseDatabase.getInstance().reference.child("usuarios").child(
            carroAtual.idUsuario
        ).child("carrinho")

        database = FirebaseDatabase.getInstance().reference

        recyclerViewServicos = findViewById(R.id.recycler_carrinho)
        recyclerViewServicos.layoutManager =  LinearLayoutManager(this)
        recyclerViewServicos.hasFixedSize()

        adapterProduto = CarrinhoAdapter(this, produtos, carrinhoRecuperados)

        recyclerViewServicos.adapter = adapterProduto

        //recupera dados
        recuperarProduto()

        var mLocale = Locale("pt", "BR")
        valor_total.addTextChangedListener(MoneyTextWatcher(valor_total, mLocale))

        btn_finalizar.setOnClickListener {
            finalizar()
        }

    }

    fun finalizar(){
        val pd = ProgressDialog(this)
        pd.setMessage("Salvando...")
        pd.show()
        val produtoRef = database.child("compras")
        var id = produtoRef.push().key
        val compras  = Compras()
        compras.valor = valor.text.toString()
        compras.custo = "R$ "+formtarDecimal(somaCusto)!!

        database.child("compras").child(id.toString()).setValue(compras).addOnCompleteListener {
            if(it.isSuccessful){
                criarPdf("Comprovante")
                Toast.makeText(this, "Compra finalizada com Sucesso", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun criarPdf(title: String?) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_TITLE, title)
        startActivityForResult(intent, CREATEPDF)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATEPDF) {
            if (data!!.data != null) {
                val caminhDoArquivo: Uri? = data.data

                val pdfDocument = PdfDocument()
                val paint = Paint()
                val pageInfo = PdfDocument.PageInfo.Builder(1240, 1754, 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas: Canvas = page.canvas
                paint.setTextAlign(Paint.Align.CENTER)
                paint.setTextSize(50f)
                paint.setFakeBoldText(true)
                canvas.drawText(
                    "Motor Place",
                    (pageInfo.pageWidth / 2).toFloat(),
                    80.toFloat(),
                    paint
                )
                paint.setTextSize(40f)
                canvas.drawText(
                    "Comprovante de compras",
                    (pageInfo.pageWidth / 2).toFloat(),
                    180.toFloat(),
                    paint
                )
                paint.setTextAlign(Paint.Align.LEFT)
                paint.setTextSize(30f)
                paint.setFakeBoldText(false)



                canvas.drawLine(
                    50.toFloat(),
                    110.toFloat(),
                    (pageInfo.pageWidth - 100).toFloat(),
                    110.toFloat(),
                    paint
                )
                canvas.drawLine(
                    50.toFloat(),
                    210.toFloat(),
                    (pageInfo.pageWidth - 100).toFloat(),
                    210.toFloat(),
                    paint
                )
                canvas.drawLine(
                    50.toFloat(),
                    410.toFloat(),
                    (pageInfo.pageWidth - 100).toFloat(),
                    410.toFloat(),
                    paint
                )



                paint.setFakeBoldText(true)
                canvas.drawText("Produtos solicitados:", 50.toFloat(), 460.toFloat(), paint)
                paint.setFakeBoldText(false)
                var espac = 460+60
                for (i in 0..(produtos.size-1)){
                    canvas.drawText(produtos.get(i).titulo, 50.toFloat(), espac.toFloat(), paint)
                    paint.setTextAlign(Paint.Align.RIGHT)
                    canvas.drawText(
                        produtos.get(i).valor,
                        (pageInfo.pageWidth - 100).toFloat(),
                        espac.toFloat(),
                        paint
                    )
                    paint.setTextAlign(Paint.Align.LEFT)
                    espac+= 40
                }




                paint.setFakeBoldText(true)
                paint.setTextSize(40f)
                canvas.drawLine(
                    50.toFloat(),
                    espac.toFloat(),
                    (pageInfo.pageWidth - 100).toFloat(),
                    espac.toFloat(),
                    paint
                )
                espac+= 60
                canvas.drawText("Total:", 50.toFloat(), espac.toFloat(), paint)
                paint.setTextAlign(Paint.Align.RIGHT)
                canvas.drawText(
                    valor.text.toString(),
                    (pageInfo.pageWidth - 100).toFloat(),
                    espac.toFloat(),
                    paint
                )
                paint.setTextAlign(Paint.Align.LEFT)
                espac+= 30
                canvas.drawLine(
                    50.toFloat(),
                    espac.toFloat(),
                    (pageInfo.pageWidth - 100).toFloat(),
                    espac.toFloat(),
                    paint
                )


                paint.setTextSize(30f)
                paint.setFakeBoldText(false)

                paint.setTextAlign(Paint.Align.RIGHT)
                canvas.drawText(
                    userAtual.nome,
                    (pageInfo.pageWidth - 100).toFloat(),
                    250.toFloat() + 20,
                    paint
                )
                canvas.drawText(
                    userAtual.telefone,
                    (pageInfo.pageWidth - 100).toFloat(),
                    300.toFloat() + 20,
                    paint
                )
                val random = Random()
                val num = random.nextInt(8000)
                canvas.drawText(
                    num.toString(),
                    (pageInfo.pageWidth - 100).toFloat(),
                    350.toFloat() + 20,
                    paint
                )





                paint.setTextAlign(Paint.Align.LEFT)
                paint.color = Color.rgb(27, 158, 218)

                canvas.drawText("Nome:", 50.toFloat(), 250.toFloat() + 20, paint)
                canvas.drawText("Telefone:", 50.toFloat(), 300.toFloat() + 20, paint)
                canvas.drawText("Código:", 50.toFloat(), 350.toFloat() + 20, paint)



                pdfDocument.finishPage(page)
                gravarPdf(caminhDoArquivo, pdfDocument)

            }
        }
    }


    private fun gravarPdf(caminhDoArquivo: Uri?, pdfDocument: PdfDocument) {
        try {
            val stream = BufferedOutputStream(
                Objects.requireNonNull(
                    contentResolver.openOutputStream(caminhDoArquivo!!)
                )
            )
            pdfDocument.writeTo(stream)
            pdfDocument.close()
            stream.flush()
            carrinhoRecuperados.removeValue()
            Toast.makeText(this, "Comprovante salvo Com Sucesso", Toast.LENGTH_LONG).show()
            finish()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, "Erro de arquivo não encontrado", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Erro de entrada e saída", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Erro desconhecido" + e.localizedMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun formataDados(dado: String): String? {
        var newDado = dado
        newDado = dado.replace(".", "")
        newDado = newDado.replace(",", ".")
        return newDado
    }

    fun formtarDecimal(saldo: Double): String? {
        val df = DecimalFormat("0.00")
        df.setMaximumFractionDigits(2)
        return df.format(saldo)
    }
    private fun recuperarProduto() {
        carrinhoRecuperados.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                produtos.clear()
                soma = 0.00
                somaCusto = 0.00
                valor.text = soma.toString()
                for (d in dataSnapshot.children) {
                    produtosRecuperados.child(d.key.toString()).addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val prod = dataSnapshot.getValue(Produto::class.java)!!

                            soma += formataDados(prod.valor.substring(3))!!.toDouble()
                            somaCusto += formataDados(prod.custo.substring(3))!!.toDouble()
                            valor.text = formtarDecimal(soma)
                            produtos.add(prod)

                            Collections.reverse(produtos)
                            adapterProduto.notifyDataSetChanged()
                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }

                    })
                }
            }
        })
    }
}
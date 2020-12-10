package com.example.motorplace.activitys

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motorplace.R
import kotlinx.android.synthetic.main.activity_orcamento.*
import java.io.BufferedOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


class OrcamentoActivity : AppCompatActivity() {
    private val CREATEPDF = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orcamento)

        supportActionBar!!.title = "Orçamento"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        gerar_pdf.setOnClickListener {
            criarPdf("Orçamento")
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
                val nomeVeiculo: String = "Chevrolet"
                val valorVeiculo: String = "50,00"
                val descricaoVeiculo: String = "campoDescricao.getText().toString()"

                val pdfDocument = PdfDocument()
                val paint = Paint()
                val pageInfo = PdfDocument.PageInfo.Builder(1240, 1754, 1).create()
                val page = pdfDocument.startPage(pageInfo)
                val canvas: Canvas = page.canvas
                paint.setTextAlign(Paint.Align.CENTER)
                paint.setTextSize(50f)
                paint.setFakeBoldText(true)
                canvas.drawText("Motor Place", (pageInfo.pageWidth/2).toFloat(), 80.toFloat(), paint)
                paint.setTextSize(40f)
                canvas.drawText("Agendamento de veículo", (pageInfo.pageWidth/2).toFloat(), 180.toFloat(), paint)
                paint.setTextAlign(Paint.Align.LEFT)
                paint.setTextSize(30f)
                paint.setFakeBoldText(false)



                canvas.drawLine(50.toFloat(), 110.toFloat(), (pageInfo.pageWidth - 100).toFloat(), 110.toFloat(), paint)
                canvas.drawLine(50.toFloat(), 210.toFloat(), (pageInfo.pageWidth - 100).toFloat(), 210.toFloat(), paint)
                canvas.drawLine(50.toFloat(), 520.toFloat(), (pageInfo.pageWidth - 100).toFloat(), 520.toFloat(), paint)
                canvas.drawLine(50.toFloat(), 670.toFloat(), (pageInfo.pageWidth - 100).toFloat(), 670.toFloat(), paint)
                canvas.drawLine(50.toFloat(), 770.toFloat(), (pageInfo.pageWidth - 100).toFloat(), 770.toFloat(), paint)



                paint.setFakeBoldText(true)
                canvas.drawText("Serviço solicitado:", 50.toFloat(), 580.toFloat(), paint)
                paint.setFakeBoldText(false)
                canvas.drawText("Troca de óleo lubrificante", 50.toFloat(), 630.toFloat(), paint)

                paint.setFakeBoldText(true)
                paint.setTextSize(40f)
                canvas.drawText("Total:", 50.toFloat(), 730.toFloat(), paint)


                paint.setTextSize(30f)
                paint.setFakeBoldText(false)

                paint.setTextAlign(Paint.Align.RIGHT)
                canvas.drawText("Juliana Ballin", (pageInfo.pageWidth - 100).toFloat(), 250.toFloat() +20 , paint)
                canvas.drawText("(92) 98191-8603",(pageInfo.pageWidth - 100).toFloat(), 300.toFloat() +20, paint)
                canvas.drawText("98000", (pageInfo.pageWidth - 100).toFloat(), 350.toFloat() +20, paint)
                canvas.drawText("Chevrolet Onix / PHE 7470", (pageInfo.pageWidth - 100).toFloat(), 400.toFloat() +20, paint)
                canvas.drawText("18/09/2020 - 13:25:00",(pageInfo.pageWidth - 100).toFloat(), 450.toFloat() +20, paint)
                paint.setFakeBoldText(true)
                paint.setTextSize(40f)
                canvas.drawText("R$ 88,00", (pageInfo.pageWidth - 100).toFloat(), 730.toFloat(), paint)


                paint.setTextAlign(Paint.Align.LEFT)
                paint.color = Color.rgb(27,158,218)

                canvas.drawText("Nome:", 50.toFloat(), 250.toFloat() +20 , paint)
                canvas.drawText("Telefone:", 50.toFloat(), 300.toFloat() +20, paint)
                canvas.drawText("Código:", 50.toFloat(), 350.toFloat() +20, paint)
                canvas.drawText("Veículo/Placa:", 50.toFloat(), 400.toFloat() +20, paint)
                canvas.drawText("Data/Hora entrada:", 50.toFloat(), 450.toFloat() +20, paint)



//                canvas.drawText("Valor do veículo:$valorVeiculo", 50.toFloat(), 105.toFloat(), paint)
//                canvas.drawText("Descrição do veículo:$descricaoVeiculo", 50.toFloat(), 135.toFloat(), paint)
               // canvas.drawLine(50.toFloat(), 50.toFloat(), (pageInfo.pageWidth - 50).toFloat(), 90.toFloat(), paint)


//                canvas.drawLine(700.toFloat(), 110.toFloat(), (pageInfo.pageWidth - 100).toFloat(), 120.toFloat(), paint)
//                canvas.drawLine(700.toFloat(), 140.toFloat(), (pageInfo.pageWidth - 100).toFloat(), 150.toFloat(), paint)
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
            Toast.makeText(this, "PDF Gravado Com Sucesso", Toast.LENGTH_LONG).show()
        } catch (e: FileNotFoundException) {
            Toast.makeText(this, "Erro de arquivo não encontrado", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Erro de entrada e saída", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Erro desconhecido" + e.localizedMessage, Toast.LENGTH_SHORT)
                .show()
        }
    }
}

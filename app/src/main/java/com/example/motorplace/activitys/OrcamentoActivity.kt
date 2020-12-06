package com.example.motorplace.activitys

import android.content.Intent
import android.graphics.Canvas
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

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CREATEPDF) {
//            if (data!!.data != null) {
//                if (!TextUtils.isEmpty(campoNome.getText()) && !TextUtils.isEmpty(campoValor.getText()) && !TextUtils.isEmpty(
//                        campoDescricao.getText()
//                    )
//                ) {
//                    val caminhDoArquivo: Uri? = data.data
//                    val nomeVeiculo: String = "Chevrolet"
//                    val valorVeiculo: String = "50,00"
//                    val descricaoVeiculo: String = "campoDescricao.getText().toString()"
//
//                    val pdfDocument = PdfDocument()
//                    val paint = Paint()
//                    val pageInfo = PdfDocument.PageInfo.Builder(1240, 1754, 1).create()
//                    val page = pdfDocument.startPage(pageInfo)
//                    val canvas: Canvas = page.canvas
//                    paint.setTextAlign(Paint.Align.CENTER)
//                    paint.setTextSize(36f)
//                    paint.setFakeBoldText(true)
//                    canvas.drawText("Relatório Veicular", pageInfo.pageWidth / 2, 50, paint)
//                    paint.setTextAlign(Paint.Align.LEFT)
//                    paint.setTextSize(24f)
//                    paint.setFakeBoldText(false)
//
//
//                    canvas.drawText("Nome veículo:$nomeVeiculo", 50, 75, paint)
//                    canvas.drawText("Valor do veículo:$valorVeiculo", 50, 105, paint)
//                    canvas.drawText("Descrição do veículo:$descricaoVeiculo", 50, 135, paint)
//                    canvas.drawLine(48, 80, pageInfo.pageWidth - 100, 90, paint)
//                    canvas.drawLine(48, 110, pageInfo.pageWidth - 100, 120, paint)
//                    canvas.drawLine(48, 140, pageInfo.pageWidth - 100, 150, paint)
//                    pdfDocument.finishPage(page)
//                    gravarPdf(caminhDoArquivo, pdfDocument)
//                }
//            }
//        }
//    }
//
//
//    private fun gravarPdf(caminhDoArquivo: Uri?, pdfDocument: PdfDocument) {
//        try {
//            val stream = BufferedOutputStream(
//                Objects.requireNonNull(
//                    contentResolver.openOutputStream(caminhDoArquivo!!)
//                )
//            )
//            pdfDocument.writeTo(stream)
//            pdfDocument.close()
//            stream.flush()
//            Toast.makeText(this, "PDF Gravado Com Sucesso", Toast.LENGTH_LONG).show()
//        } catch (e: FileNotFoundException) {
//            Toast.makeText(this, "Erro de arquivo não encontrado", Toast.LENGTH_LONG).show()
//        } catch (e: IOException) {
//            Toast.makeText(this, "Erro de entrada e saída", Toast.LENGTH_LONG).show()
//        } catch (e: Exception) {
//            Toast.makeText(this, "Erro desconhecido" + e.localizedMessage, Toast.LENGTH_SHORT)
//                .show()
//        }
//    }
}

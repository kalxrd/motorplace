package com.example.motorplace.activitys

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.motorplace.R
import com.example.motorplace.adapter.AgendaAdapter
import com.example.motorplace.model.ServicosSolicitados
import com.example.motorplace.util.carroAtual
import com.example.motorplace.util.userAtual
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.android.synthetic.main.activity_servico.*
import java.io.BufferedOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


class ServicoActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener,DialogInterface.OnCancelListener {
    private lateinit var modelo :TextView
    private lateinit var tamanho :TextView
    private lateinit var valor :TextView
    private lateinit var titulo :TextView
    private lateinit var dados :Bundle
    private lateinit var database: DatabaseReference
    private var dia =0
    private var mes =0
    private var ano =0
    private var hora =0
    private var minuto =0
    private lateinit var txtDia :TextView
    private lateinit var txtHora :TextView
    private val CREATEPDF = 1
    private val servicos = ServicosSolicitados()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servico)
        supportActionBar!!.title = "Serviço"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        inicializar()



        btn_solicitar_servico.setOnClickListener {
            cadastroServico()
        }


        txt_dia.keyListener = null
        txt_dia.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= txt_dia.getRight() - txt_dia.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    // your action here
                    abrirCalendario()
                    return@OnTouchListener true
                }
            }
            false
        })

        txtHora.keyListener = null
        txtHora.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= txtHora.getRight() - txtHora.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    // your action here
                    abrirHora()
                    return@OnTouchListener true
                }
            }
            false
        })


    }

    private fun inicializar(){
        modelo = findViewById(R.id.txt_modelo)
        tamanho = findViewById(R.id.txt_tamanho)
        valor = findViewById(R.id.valor)
        titulo = findViewById(R.id.titulo_solicitar)
        dados = intent.extras!!
        database = FirebaseDatabase.getInstance().reference
        txtDia = findViewById(R.id.txt_dia)
        txtHora = findViewById(R.id.txt_hora)


        modelo.text = "Modelo: ${carroAtual.modelo}"
        tamanho.text = "Tamanho: ${carroAtual.tamanho}"
        valor.text = dados.getString("valor")
        titulo.text = dados.getString("titulo")

        Picasso.get()
            .load(dados.getString("foto"))
            .into(image_solicitar_servico)
    }

    fun abrirHora(){
        initDatas()
        //cria calendario atual
        val now: Calendar = Calendar.getInstance()
        now.set(ano, mes, dia,hora,minuto)

        val timePickerDialog = TimePickerDialog.newInstance(
            this,
            now.get(Calendar.HOUR_OF_DAY),
            now.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.setOnCancelListener(this)
        timePickerDialog.show(supportFragmentManager, "timePickerDialog")
        timePickerDialog.title = "Escolha um horário"

    }

    fun abrirCalendario(){
        initDatas()
        //cria calendario atual
        val now: Calendar = Calendar.getInstance()
        now.set(ano, mes, dia)

        val datePickerDialog = DatePickerDialog.newInstance(
            this,
            now.get(Calendar.YEAR), // ano inicial selecionado
            now.get(Calendar.MONTH), // mes inicial selecionado
            now.get(Calendar.DAY_OF_MONTH) // dia inicial selecionado
        )


        var cMin = Calendar.getInstance() //calendario minimo
        var cMax = Calendar.getInstance() //calendario minimo

        cMax.set(cMax.get(Calendar.YEAR), 11, 31) // seta os dados de ate o maximo que o calendario ira se movimentar
        //seta o minimo e maximo do calendario
        datePickerDialog.minDate = cMin
        datePickerDialog.maxDate = cMax


        //tira os finais de semana do calendario
        val daysList: MutableList<Calendar> = LinkedList()
        val daysArray: Array<Calendar?>
        val cAux = Calendar.getInstance()
        while (cAux.timeInMillis <= cMax.timeInMillis) {
            if (cAux[Calendar.DAY_OF_WEEK] !== 1 && cAux[Calendar.DAY_OF_WEEK] !== 7) {
                val c = Calendar.getInstance()
                c.timeInMillis = cAux.timeInMillis
                daysList.add(c)
            }
            cAux.timeInMillis = cAux.timeInMillis + 24 * 60 * 60 * 1000
        }
        daysArray = arrayOfNulls(daysList.size)
        for (i in daysArray.indices) {
            daysArray[i] = daysList[i]
        }

        datePickerDialog.setSelectableDays(daysArray)
        datePickerDialog.setOnCancelListener(this)

        //exibe o calendario
        datePickerDialog.show(supportFragmentManager, "Datepickerdialog")
    }

    fun initDatas(){
        if(ano == 0){
            //inicia os dados do com as datas atuais
           var c = Calendar.getInstance()
            ano = c.get(Calendar.YEAR)
            mes = c.get(Calendar.MONTH)
            dia = c.get(Calendar.DAY_OF_MONTH)
            hora = c.get(Calendar.HOUR_OF_DAY)
            minuto = c.get(Calendar.MINUTE)
        }
    }

    fun cadastroServico(){
        var valido = true
        if (txtDia.text.toString().isEmpty()){
            valido = false
        }
        if (txtHora.text.toString().isEmpty()){
            valido = false
        }

        if (valido){
            val pd = ProgressDialog(this)
            pd.setMessage("Salvando...")
            pd.show()


            servicos.idCliente = carroAtual.idUsuario
            servicos.idServico = dados.getString("id")!!
            servicos.data = txtDia.text.toString()
            servicos.hora = txtHora.text.toString()
            servicos.nomeCliente = userAtual.nome

            val idServicosSolicitados =  database.child("servicosSolicitados").push().key
            servicos.id = idServicosSolicitados.toString()

            database.child("servicos").child(servicos.idServico).child("servicosSolicitados").child(
                idServicosSolicitados.toString()
            ).setValue(servicos).addOnCompleteListener {
                if(it.isSuccessful){
                    baixarPdf()
                    Toast.makeText(this, "Serviço solicitado com Sucesso", Toast.LENGTH_SHORT).show()
                }

            }
        }else{
            Toast.makeText(this, "Preencha todos os dados", Toast.LENGTH_SHORT).show();
        }
    }
    fun baixarPdf(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Download")
        builder.setMessage("Você deseja baixar o pdf do seu orçamento ?")
        builder.setCancelable(false)

        builder.setPositiveButton("Sim" ){ dialogInterface, i -> criarPdf("Orçamento")}

        builder.setNegativeButton("Não"){dialogInterface, i -> finish()}

        val dialog = builder.create()
        dialog.show()
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
                canvas.drawText(dados.getString("titulo")!!, 50.toFloat(), 630.toFloat(), paint)

                paint.setFakeBoldText(true)
                paint.setTextSize(40f)
                canvas.drawText("Total:", 50.toFloat(), 730.toFloat(), paint)


                paint.setTextSize(30f)
                paint.setFakeBoldText(false)

                paint.setTextAlign(Paint.Align.RIGHT)
                canvas.drawText(userAtual.nome, (pageInfo.pageWidth - 100).toFloat(), 250.toFloat() +20 , paint)
                canvas.drawText(userAtual.telefone,(pageInfo.pageWidth - 100).toFloat(), 300.toFloat() +20, paint)
                val random = Random()
                val num = random.nextInt(8000)

                canvas.drawText(num.toString(), (pageInfo.pageWidth - 100).toFloat(), 350.toFloat() +20, paint)
                canvas.drawText(carroAtual.modelo +" " + carroAtual.marca +" / "+ carroAtual.placa, (pageInfo.pageWidth - 100).toFloat(), 400.toFloat() +20, paint)
                canvas.drawText(servicos.data+" - "+ txtHora.text.toString(),(pageInfo.pageWidth - 100).toFloat(), 450.toFloat() +20, paint)
                paint.setFakeBoldText(true)
                paint.setTextSize(40f)
                canvas.drawText(valor.text.toString(), (pageInfo.pageWidth - 100).toFloat(), 730.toFloat(), paint)


                paint.setTextAlign(Paint.Align.LEFT)
                paint.color = Color.rgb(27,158,218)

                canvas.drawText("Nome:", 50.toFloat(), 250.toFloat() +20 , paint)
                canvas.drawText("Telefone:", 50.toFloat(), 300.toFloat() +20, paint)
                canvas.drawText("Código:", 50.toFloat(), 350.toFloat() +20, paint)
                canvas.drawText("Veículo/Placa:", 50.toFloat(), 400.toFloat() +20, paint)
                canvas.drawText("Data/Hora entrada:", 50.toFloat(), 450.toFloat() +20, paint)

                
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

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var dia = ""
        var mes = ""
        if(dayOfMonth < 10){
            dia = "0"+dayOfMonth.toString()
        }else{
            dia = dayOfMonth.toString()
        }

        if ((monthOfYear+1) < 10){
            mes = "0"+(monthOfYear+1).toString()
        }else{
            mes = (monthOfYear+1).toString()
        }

        txtDia.text = dia + mes + year.toString()
    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        var min = ""
        var hora = ""
        if( hourOfDay < 8 || hourOfDay >= 18   ){
            Toast.makeText(this, "Somente entre 8h e 18h", Toast.LENGTH_SHORT).show();
            return;
        }else{
            //linhas abaixo para formatar
            if(minute >=0 && minute <=9){
                min = "0"+minute.toString()
            }else{
                min =minute.toString()
            }

            if(hourOfDay >=8 && hourOfDay <=9){
                hora = "0"+ hourOfDay.toString()
            }else{
                hora = hourOfDay.toString()
            }

            txtHora.text = hora + min
        }



    }

    override fun onCancel(dialog: DialogInterface?) {

    }
}
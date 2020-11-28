package com.example.motorplace.fragments.adm


import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.motorplace.R
import com.example.motorplace.adapter.AgendaAdmAdapter
import com.example.motorplace.model.Servico
import com.example.motorplace.model.ServicosSolicitados
import com.example.motorplace.util.datasSolicitadas
import com.example.motorplace.util.servicosGlobal
import com.example.motorplace.util.servicosSolicitadosGlobal
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import kotlinx.android.synthetic.main.fragment_agenda_adm.*
import kotlinx.android.synthetic.main.fragment_agenda_adm.view.*
import java.text.SimpleDateFormat
import java.util.*

class AgendaAdmFragment : Fragment() {
    private lateinit var recyclerViewServicos: RecyclerView
    private lateinit var adapterAgenda: AgendaAdmAdapter
    var servicosSolicitadosAux = arrayListOf<ServicosSolicitados>()
    var servicosAux = arrayListOf<Servico>()
    var mes = ""

    private lateinit var compactCalendar: CompactCalendarView
    private val dateFormatMonth: SimpleDateFormat = SimpleDateFormat(
        "MMMM- YYYY",
        Locale.getDefault()
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_agenda_adm, container, false)


        compactCalendar = view.findViewById(R.id.compactcalendar_view)
        compactCalendar.setUseThreeLetterAbbreviation(true)
        recyclerViewServicos = view.findViewById(R.id.recycler_calendario)



        mes =  dateFormatMonth.format(compactCalendar.firstDayOfCurrentMonth).toString().substring(0,1).toUpperCase() + dateFormatMonth.format(compactCalendar.firstDayOfCurrentMonth).toString().substring(1)
        view.mes_calendario.text = mes

        selecionaDatas()

        compactCalendar.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                servicosSolicitadosAux.clear()
                servicosAux.clear()
                var anoConvert = dateClicked.year - 100
                var data = dateClicked.date.toString() +"/"+ (dateClicked.month+1)+"/20"+anoConvert
                for (x in 0 until datasSolicitadas.size){
                    if (data.equals(datasSolicitadas.get(x))) {
                        exibirServicos(servicosSolicitadosGlobal.get(x), servicosGlobal.get(x),dateClicked.day.toString())
                    }
                }
                adapterAgenda.notifyDataSetChanged()
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                mes =  dateFormatMonth.format(firstDayOfNewMonth).toString().substring(0,1).toUpperCase() + dateFormatMonth.format(firstDayOfNewMonth).toString().substring(1)
                view.mes_calendario.text = mes
            }
        })


        recyclerViewServicos.layoutManager = LinearLayoutManager(context)
        recyclerViewServicos.hasFixedSize()

        adapterAgenda = AgendaAdmAdapter(view.context!!, servicosSolicitadosAux,servicosAux)

        recyclerViewServicos.adapter = adapterAgenda

        return view
    }

    fun selecionaDatas(){
        for (x in 0 until datasSolicitadas.size){
            var c = Calendar.getInstance()
            c.set(datasSolicitadas.get(x).substring(6,10).toInt(),datasSolicitadas.get(x).substring(3,5).toInt()-1, datasSolicitadas.get(x).substring(0,2).toInt())
            var ev1 = Event(Color.rgb(128,0,0), c.timeInMillis, "Teachers' Professional Day")
            compactCalendar.addEvent(ev1)
        }
    }

    fun exibirServicos(listServicos:ServicosSolicitados,listServicos2:Servico, dia:String){

        when(dia){
            "1"-> {
                listServicos.diaSemana  = "Segunda-feira"
            }
            "2"-> {
                listServicos.diaSemana  = "Terça-feira"
            }
            "3"-> {
                listServicos.diaSemana  = "Quarta-feira"
            }
            "4"-> {
                listServicos.diaSemana  = "Quinta-feira"
            }
            "5"-> {
                listServicos.diaSemana  = "Sexta-feira"
            }
            "6"-> {
                listServicos.diaSemana  = "Sábado"
            }
            "0"-> {
                listServicos.diaSemana  = "Domingo"
            }

        }
        servicosSolicitadosAux.add(listServicos)
        servicosAux.add(listServicos2)
    }

}
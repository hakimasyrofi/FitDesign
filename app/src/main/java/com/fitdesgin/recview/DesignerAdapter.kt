package com.fitdesgin.recview

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitdesgin.ChatActivity
import com.fitdesgin.DetailPaymentActivity
import com.fitdesgin.R
import java.util.*


class DesignerAdapter(private val designerList: ArrayList<Designer>) : RecyclerView.Adapter<DesignerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DesignerAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_designer,
            parent,
            false
        )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return designerList.size
    }

    override fun onBindViewHolder(holder: DesignerAdapter.MyViewHolder, position: Int) {
        val designer : Designer = designerList[position]
        holder.fullName.text = designer.fullName
        holder.experience.text = designer.experience.toString()+" tahun"
        Glide.with(holder.itemView.context)
                .load(designer.photo)
                .into(holder.photo)
        //.apply(RequestOptions().override(80,120))
        holder.buttonChat.setOnClickListener{
            val intent = Intent(it.context, DetailPaymentActivity::class.java)
            intent.putExtra(DetailPaymentActivity.EXTRA_NAME, designer.fullName)
            intent.putExtra(DetailPaymentActivity.EXTRA_PHOTO, designer.photo)

            val mySharedPreferences: SharedPreferences =
                it.context.getSharedPreferences("MYPREFERENCENAME", Context.MODE_PRIVATE)
            val editor = mySharedPreferences.edit()
            editor.putString(ChatActivity.EXTRA_NAME, designer.fullName)
            editor.putString(ChatActivity.EXTRA_PHOTO, designer.photo)
            editor.apply()

            it.context.startActivity(intent)
            //Toast.makeText(holder.itemView.context, "Share " + designerList[holder.adapterPosition].fullName, Toast.LENGTH_SHORT).show()
        }

    }

    // Ini class baru yang dibuat
    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val fullName : TextView = itemView.findViewById(R.id.tv_fullName)
        val experience : TextView = itemView.findViewById(R.id.tv_experience)
        val photo : ImageView = itemView.findViewById(R.id.iv_designer)
        val buttonChat: Button = itemView.findViewById(R.id.chat)
    }
}
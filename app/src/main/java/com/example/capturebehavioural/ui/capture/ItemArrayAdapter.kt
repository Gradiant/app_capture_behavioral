package com.example.capturebehavioural.ui.capture

import android.content.Context
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.capturebehavioural.R
import com.example.capturebehavioural.databinding.ListItemBinding
import com.example.domain.ListItem
import java.text.Normalizer

class ItemArrayAdapter(private val listItemClickListener: ListItemClickListener, private val context: Context) : ListAdapter<ListItem, ItemArrayAdapter.ListenItemViewHolder>(ListItemCallback()) {

    private var iteration = 0

    class ListItemCallback : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return oldItem.ask == newItem.ask && oldItem.response == newItem.response
        }
    }

    interface ListItemClickListener {
        fun onItemClick(iteration: Int)
    }


    inner class ListenItemViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listItem: ListItem) {
            if (listItem.response.size == 4) {
                binding.btSend.visibility = View.GONE
                binding.etResponse.visibility = View.GONE
                binding.tvAsk.text = listItem.ask
                binding.btResponseOne.text = listItem.response[0]
                binding.btResponseTwo.text = listItem.response[1]
                binding.btResponseThree.text = listItem.response[2]
                binding.btResponseFour.text = listItem.response[3]
                binding.btResponseOne.visibility = View.VISIBLE
                binding.btResponseTwo.visibility = View.VISIBLE
                binding.btResponseThree.visibility = View.VISIBLE
                binding.btResponseFour.visibility = View.VISIBLE
            } else {
                binding.btSend.visibility = View.VISIBLE
                binding.btResponseOne.visibility = View.GONE
                binding.btResponseTwo.visibility = View.GONE
                binding.btResponseThree.visibility = View.GONE
                binding.btResponseFour.visibility = View.GONE
                binding.etResponse.visibility = View.VISIBLE
                binding.tvAsk.text = listItem.ask
            }
            binding.btResponseOne.setOnClickListener {
                binding.btResponseOne.background = ContextCompat.getDrawable(context, R.color.red)
                responseButtons(listItem, binding)
                iteration++
                listItemClickListener.onItemClick(iteration)
            }
            binding.btResponseTwo.setOnClickListener {
                binding.btResponseTwo.background = ContextCompat.getDrawable(context, R.color.red)
                responseButtons(listItem, binding)
                iteration++
                listItemClickListener.onItemClick(iteration)
            }

            binding.btResponseThree.setOnClickListener {
                binding.btResponseThree.background = ContextCompat.getDrawable(context, R.color.red)
                responseButtons(listItem, binding)
                iteration++
                listItemClickListener.onItemClick(iteration)
            }
            binding.btResponseFour.setOnClickListener {
                binding.btResponseFour.background = ContextCompat.getDrawable(context, R.color.red)
                responseButtons(listItem, binding)
                iteration++
                listItemClickListener.onItemClick(iteration)
            }

            binding.btSend.setOnClickListener {
                for (response in listItem.response) {
                    if (binding.etResponse.text.toString().trim().lowercase()
                            .unaccent() == response.lowercase().unaccent()
                    ) {
                        binding.etResponse.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            ContextCompat.getDrawable(context, R.drawable.ic_check),
                            null
                        )
                    } else {
                        binding.etResponse.setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            ContextCompat.getDrawable(context, R.drawable.ic_error),
                            null
                        )
                    }
                }
                binding.etResponse.isEnabled = false
                binding.btSend.isEnabled = false
                iteration++
                listItemClickListener.onItemClick(iteration)
            }
        }
    }

    fun responseButtons(listItem: ListItem, binding: ListItemBinding) {
        listItem.response.forEachIndexed { index, s ->
            if (s == listItem.correct[0]) {
                when (index) {
                    0 -> binding.btResponseOne.background =
                        ContextCompat.getDrawable(context, R.color.green)
                    1 -> binding.btResponseTwo.background =
                        ContextCompat.getDrawable(context, R.color.green)
                    2 -> binding.btResponseThree.background =
                        ContextCompat.getDrawable(context, R.color.green)
                    3 -> binding.btResponseFour.background =
                        ContextCompat.getDrawable(context, R.color.green)
                }
            }

            binding.btResponseOne.isEnabled = false
            binding.btResponseTwo.isEnabled = false
            binding.btResponseThree.isEnabled = false
            binding.btResponseFour.isEnabled = false
        }
    }

    private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

    fun CharSequence.unaccent(): String {
        val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
        return REGEX_UNACCENT.replace(temp, "")
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListenItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListenItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListenItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

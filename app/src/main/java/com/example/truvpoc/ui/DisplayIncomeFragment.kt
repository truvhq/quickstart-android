package com.example.truvpoc.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.truvpoc.MainViewModel
import com.example.truvpoc.R
import com.example.truvpoc.api.data.Employment
import com.example.truvpoc.databinding.FragmentIncomeBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DisplayIncomeFragment : Fragment(R.layout.fragment_income) {

    private val viewModel: MainViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private lateinit var binding: FragmentIncomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIncomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.incomeInfo
            .filterNotNull()
            .onEach(::setFormValues)
            .launchIn(lifecycleScope)
    }

    private fun setFormValues(employment: Employment) {
        binding.textFirstName.setText(employment.profile.firstName)
        binding.textLastName.setText(employment.profile.lastName)
        binding.textSSN.setText(employment.profile.ssn)
        binding.textDateOfBirth.setText(employment.profile.dateOfBirth)

        binding.textEmployerName.text = employment.company.name
        binding.textEmployerCity.setText(employment.company.address?.city)
        binding.textEmployerState.setText(employment.company.address?.state)
        binding.textEmployerPhone.setText(employment.company.phone)

        binding.textStartDate.setText(employment.startDate)
        binding.textEndDate.setText(employment.endDate)
        binding.textPositionType.setText(employment.jobType)
        binding.textTitle.setText(employment.jobTitle)

        binding.textAnnualIncome.setText(employment.annualSalary)
    }

}
package com.example.unitconverter.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unitconverter.R
import com.example.unitconverter.viewmodels.WeightViewModel

@Composable
fun WeightConverter() {
    val viewModel: WeightViewModel = viewModel()
    val strKg = stringResource(id = R.string.kg)
    val strPound = stringResource(id = R.string.pound)
    val currentValue = viewModel.weight.observeAsState(viewModel.weight.value ?: "")
    val unit = viewModel.unit.observeAsState(viewModel.unit.value ?: R.string.kg)
    var result by rememberSaveable { mutableStateOf("") }
    val calc = {
        val temp = viewModel.convert()
        result = if (temp.isNaN())
            ""
        else
            "$temp${
                if (unit.value == R.string.kg)
                    " $strPound"
                else " $strKg"
            }"
    }
    val enabled by remember(currentValue.value) {
        mutableStateOf(!viewModel.getWeightAsFloat().isNaN())
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeightTextField(
            weight = currentValue,
            modifier = Modifier.padding(bottom = 16.dp,top = 32.dp),
            callback = calc,
            viewModel = viewModel
        )
        WeightScaleButtonGroup(
            selected = unit,
            modifier = Modifier.padding(bottom = 16.dp)
        ) { resId: Int ->
            viewModel.setUnit(resId)
        }
        Button(
            onClick = calc,
            enabled = enabled
        ) {
            Text(text = stringResource(id = R.string.convert))
        }
        if (result.isNotEmpty()) {
            Text(
                text = result,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}

@Composable
fun WeightTextField(
    weight: State<String>,
    modifier: Modifier = Modifier,
    callback: () -> Unit,
    viewModel: WeightViewModel
) {
    TextField(
        value = weight.value,
        onValueChange = {
            viewModel.setWeight(it)
        },
        placeholder = {
            Text(text = stringResource(id = R.string.placeholder_weight))
        },
        modifier = modifier,
        keyboardActions = KeyboardActions(onAny = {
            callback()
        }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}

@Composable
fun WeightScaleButtonGroup(
    selected: State<Int>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    val sel = selected.value
    Row(modifier = modifier) {
        WeightRadioButton(
            selected = sel == R.string.kg,
            resId = R.string.kg,
            onClick = onClick
        )
        WeightRadioButton(
            selected = sel == R.string.pound,
            resId = R.string.pound,
            onClick = onClick,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun WeightRadioButton(
    selected: Boolean,
    resId: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onClick(resId)
            }
        )
        Text(
            text = stringResource(resId),
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}
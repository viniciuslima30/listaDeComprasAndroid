package com.example.listadecompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class Produto(
    val nome: String,
    val preco: Double,
    val quantidade: Int
) {
    fun calcularTotal(): Double {
        return preco * quantidade
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Usamos o Surface do MaterialTheme padrão para substituir o tema que sumiu
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val produtos = remember { mutableStateListOf<Produto>() }
                    var total by remember { mutableStateOf(0.0) }

                    Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                        Text(
                            text = "Lista de Compras",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // 1. Componente de Entrada de Dados
                        Formulario(
                            onProdutoCadastrado = { novoProduto ->
                                produtos.add(novoProduto)
                                total += novoProduto.calcularTotal()
                            }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // 2. Componente da Lista de Produtos
                        ListaProdutos(produtos = produtos)

                        Spacer(modifier = Modifier.height(24.dp))

                        // 3. Componente de Resumo Financeiro
                        Resumo(total = total)
                    }
                }
            }
        }
    }
}

// --- COMPONENTES SEPARADOS ---

@Composable
fun Formulario(onProdutoCadastrado: (Produto) -> Unit) {
    var nome by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }

    Column {
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do produto") }
        )

        TextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text("Preço") }
        )

        TextField(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text("Quantidade") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val precoDouble = preco.toDoubleOrNull() ?: 0.0
                val qtdInt = quantidade.toIntOrNull() ?: 0

                if (nome.isNotBlank() && precoDouble > 0 && qtdInt > 0) {
                    val produto = Produto(
                        nome = nome,
                        preco = precoDouble,
                        quantidade = qtdInt
                    )
                    onProdutoCadastrado(produto)

                    // Limpa os campos após cadastrar
                    nome = ""
                    preco = ""
                    quantidade = ""
                }
            }
        ) {
            Text("Cadastrar")
        }
    }
}

@Composable
fun ListaProdutos(produtos: List<Produto>) {
    Column {
        Text(
            text = "Produtos Cadastrados:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        for (produto in produtos) {
            Text(
                "${produto.nome} - " +
                        "${produto.quantidade} x R$ ${produto.preco} = " +
                        "R$ ${produto.calcularTotal()}"
            )
        }
    }
}

@Composable
fun Resumo(total: Double) {
    val totalFormatado = String.format("%.2f", total)
    Text(
        text = "Total da compra: R$ $totalFormatado",
        style = MaterialTheme.typography.titleLarge
    )
}

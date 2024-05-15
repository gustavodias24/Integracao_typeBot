package com.example.treinarai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treinarai.model.ResponseModel;
import com.example.treinarai.model.UserModel;
import com.example.treinarai.retrofitUtil.HackUtill;
import com.example.treinarai.retrofitUtil.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaCadastro extends AppCompatActivity {
    private Dialog dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro); // Definindo o layout da tela de cadastro

        dialogLoading = HackUtill.instanciarDialogLogin(TelaCadastro.this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // questão de estética by: Benicio

        // Encontrando os campos de entrada e o botão no layout
        EditText campoNomeCad = findViewById(R.id.campoNomeCad);
        EditText campoEmail = findViewById(R.id.campoEmailCad);
        EditText campoConfEmailCad = findViewById(R.id.campoConfEmailCad);
        EditText campoUsernameCad = findViewById(R.id.campoUsernameCad);
        EditText campoSenhaCad = findViewById(R.id.campoSenhaCad);
        EditText campoConfSenhaCad = findViewById(R.id.campoConfSenhaCad);
        Button botaoEnviar = findViewById(R.id.botaoEnviar);

        // Configurando os campos de senha para ocultar o texto digitado
        campoSenhaCad.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        campoConfSenhaCad.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // Definindo um listener de clique para o botão "Enviar"
        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica de validação dos campos de cadastro
                if (validarCampos()) {

                    UserModel usuarioCadastro = new UserModel();
                    usuarioCadastro.setNomeCompleto(
                            campoNomeCad.getText().toString()
                    );
                    usuarioCadastro.setUsername(
                            campoUsernameCad.getText().toString()
                    );
                    usuarioCadastro.setEmail(
                            campoEmail.getText().toString()
                    );
                    usuarioCadastro.setSenha(
                            HackUtill.encriptografarSenha(campoSenhaCad.getText().toString())
                    );


                    // Obtendo o nome de usuário digitado
                    String username = campoUsernameCad.getText().toString().trim();

                    // Criando a Intent para abrir a tela principal
                    Intent intent = new Intent(TelaCadastro.this, activity_tela_principal.class);

                    // Passando o nome de usuário para a tela principal como um extra
                    intent.putExtra("USERNAME", username);


                    /*
                     *
                     *  Lógica de cadastro feito pela api, estou instânciando o retrofit para acessar a rota
                     * que vai fazer cadastro  do usuáro no sistema.
                     *
                     * */

                    dialogLoading.show();
                    RetrofitUtil.intanceService(
                            RetrofitUtil.instanceRetrofit()
                    ).criarUser(usuarioCadastro).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            dialogLoading.dismiss();

                            if (response.isSuccessful()) {
                                // Iniciando a nova atividade caso o cadastro for bem sucedido
                                Toast.makeText(TelaCadastro.this, response.body().getResp(), Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable throwable) {
                            dialogLoading.dismiss();
                            Toast.makeText(TelaCadastro.this, "Problema de Conexão!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });


        // Encontrando o ícone de texto no layout
        TextView tituloTreinarAI2 = findViewById(R.id.tituloTreinarAI2);

        // Definindo um listener de clique para o ícone de texto
        tituloTreinarAI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar de volta para a tela de login (MainActivity)
                Intent intent = new Intent(TelaCadastro.this, MainActivity.class);
                startActivity(intent);
                finish(); // Encerra a atividade atual para evitar acumulação na pilha de atividades
            }
        });
    }

    private boolean validarCampos() {
        // Campos de entrada
        EditText campoNomeCad = findViewById(R.id.campoNomeCad);
        EditText campoEmail = findViewById(R.id.campoEmailCad);
        EditText campoConfEmailCad = findViewById(R.id.campoConfEmailCad);
        EditText campoUsernameCad = findViewById(R.id.campoUsernameCad);
        EditText campoSenhaCad = findViewById(R.id.campoSenhaCad);
        EditText campoConfSenhaCad = findViewById(R.id.campoConfSenhaCad);

        // Obtendo valores dos campos
        String nome = campoNomeCad.getText().toString().trim();
        String email = campoEmail.getText().toString().trim();
        String confEmail = campoConfEmailCad.getText().toString().trim();
        String senha = campoSenhaCad.getText().toString().trim();
        String confSenha = campoConfSenhaCad.getText().toString().trim();

        // Verificando se algum campo está vazio
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(confEmail)
                || TextUtils.isEmpty(senha) || TextUtils.isEmpty(confSenha)) {
            Toast.makeText(TelaCadastro.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificando se os emails são válidos
        if (!isValidEmail(email) || !isValidEmail(confEmail)) {
            Toast.makeText(TelaCadastro.this, "Por favor, insira um email válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificando se os emails coincidem
        if (!email.equals(confEmail)) {
            Toast.makeText(TelaCadastro.this, "Os emails não coincidem", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificando se as senhas têm pelo menos 8 caracteres
        if (!isValidPassword(senha) || !isValidPassword(confSenha)) {
            Toast.makeText(TelaCadastro.this, "A senha deve conter pelo menos 8 caracteres, incluindo letras e números", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verificando se as senhas coincidem
        if (!senha.equals(confSenha)) {
            Toast.makeText(TelaCadastro.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Método para verificar se o email é válido usando expressão regular
    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    // Método para verificar se a senha atende aos critérios mínimos
    private boolean isValidPassword(String password) {
        // Padrão que verifica se a senha contém pelo menos 8 caracteres, incluindo letras e números
        return password != null && password.length() >= 8 && password.matches(".*[A-Za-z].*") && password.matches(".*\\d.*");
    }
}

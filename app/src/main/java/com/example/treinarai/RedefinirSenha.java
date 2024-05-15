package com.example.treinarai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.treinarai.model.ResponseModel;
import com.example.treinarai.model.UserModel;
import com.example.treinarai.retrofitUtil.HackUtill;
import com.example.treinarai.retrofitUtil.RetrofitUtil;
import com.example.treinarai.retrofitUtil.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedefinirSenha extends AppCompatActivity {

    private Dialog dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redefinir_senha); // Definindo o layout da tela de redefinição de senha

        dialogLoading = HackUtill.instanciarDialogLogin(RedefinirSenha.this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // questão de estética by: Benicio

        // Encontrando os campos de entrada e os botões no layout
        EditText campoEmailRed = findViewById(R.id.campoEmailRed);
        EditText campoSenhaRed = findViewById(R.id.campoSenhaRed);
        Button botaoRedefinirSenha = findViewById(R.id.botaoRedefinirSenha);
        Button botaoVoltarTelaLogin = findViewById(R.id.botaoVoltarTelaLogin);

        // Definindo um listener de clique para o botão "Redefinir Senha"
        botaoRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter texto dos campos de email e senha
                String email = campoEmailRed.getText().toString().trim();
                String senha = campoSenhaRed.getText().toString().trim();

                // Verificar se os campos estão vazios
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                    // Mostrar uma mensagem de erro se algum dos campos estiver vazio
                    Toast.makeText(RedefinirSenha.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Lógica para redefinir a senha (opcional)

                    UserModel trocarSenhaUser = new UserModel();
                    trocarSenhaUser.setEmail(email);
                    trocarSenhaUser.setNovaSenha(HackUtill.encriptografarSenha(senha));

                    /*
                    instanciando rapidamente um retrofit e chamando a função que vai atualizar
                    a senha no banco de dados do usuario.
                    * */

                    dialogLoading.show();
                    RetrofitUtil.intanceService(
                            RetrofitUtil.instanceRetrofit()
                    ).atualizarUser(trocarSenhaUser).enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            dialogLoading.dismiss();

                            if (response.isSuccessful()) {
                                // Navegar de volta para a tela de login (MainActivity)
                                Toast.makeText(RedefinirSenha.this, response.body().getResp(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RedefinirSenha.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Encerra a atividade atual para evitar acumulação na pilha de atividades
                            }else{
                                Toast.makeText(RedefinirSenha.this, "E-mail não Encontrado no Banco de Dados!", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable throwable) {
                            dialogLoading.dismiss();
                            Toast.makeText(RedefinirSenha.this, "Problema de Conexão!", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

        // Definindo um listener de clique para o botão "Voltar para a tela de login"
        botaoVoltarTelaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar de volta para a tela de login (MainActivity)
                Intent intent = new Intent(RedefinirSenha.this, MainActivity.class);
                startActivity(intent);
                finish(); // Encerra a atividade atual para evitar acumulação na pilha de atividades
            }
        });

        // Encontrando o ícone de texto no layout
        TextView tituloTreinarAI2 = findViewById(R.id.tituloTreinarAI4);

        // Definindo um listener de clique para o ícone de texto
        tituloTreinarAI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar de volta para a tela de login (MainActivity)
                Intent intent = new Intent(RedefinirSenha.this, MainActivity.class);
                startActivity(intent);
                finish(); // Encerra a atividade atual para evitar acumulação na pilha de atividades
            }
        });
    }
}

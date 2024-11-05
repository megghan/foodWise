# FOODWISE 🥗
## O QUE É?
Projeto para a matéria de UPX. App voltado para gerenciamento de alimentos e rastreamento de validade, visando diminuir a perda de alimentos e incentivar uma alimentação saudável.

## DEVLOG
### Master Branch
Criado o projeto Java usando a IDE IntelliJ. 
Drivers adicionados: JavaFX e MySQL JDBC, para exibição de UI e conecção com MySQL.

### Database branch: foodwise 0.1
Criado o ramo "database" a partir de onde vamos realizar os trabalhos.
Criamos um Banco de dados inicial:

![image](https://github.com/user-attachments/assets/ab717ecf-944c-4391-97d1-5bf356a61bc7)

E depois conectamos à CLasse MySQLConnection.java usando a IDE IntelliJ

![image](https://github.com/user-attachments/assets/781a1a20-4af6-4837-8029-e10fd1f51ff8)

Enquanto o desenvolvimento do código e parte teórica do projeto estava sendo desenvolvido pelos outros membros da equipe o protótipo das telas foi criado no Figma --->

Testando a aplicação no console.

![image](https://github.com/user-attachments/assets/71a1df4c-ea74-49ee-89d1-434c204cee2e)

As duas classes iniciais nesse ponto do projeto:

![image](https://github.com/user-attachments/assets/f0b6403f-3d20-4de0-a4c7-69981107a909)

Na versão atual a classe AppFoodWise.java foi posta de lado, ela era um protótipo para a classe final onde implementei a interface gráfica.

Estes foram os primeiros passos na lógica, neste ponto ainda não existe a tela de login e nem uma forma de atrelar os itens criados ao login do usuario. Esse problema foi resolvido mais tarde.

####Banco de dados em detalhes
![image](https://github.com/user-attachments/assets/36267b3d-0464-4437-bff9-2872f9acf573)
![image](https://github.com/user-attachments/assets/4962b4d0-5932-4b33-bfa4-d4f6e4109140)
![image](https://github.com/user-attachments/assets/48d98480-5bfe-4b7f-9626-bfc97af0d1e5)

Os itens criados e adicionados atraves da Aplicação

![image](https://github.com/user-attachments/assets/369e39c3-cef2-4184-ad35-b7995b602097)

### Display Branch foodwise 0.2
Aqui foi onde comecei a implementar a interface gráfica e a lógica de login com a funcionalidade correta.
Aqui o banco de dados mudou, se tornando mais robusto levando em conta o id de usuario na hora de criar receitas e comida.
exemplo:

![image](https://github.com/user-attachments/assets/538d5b41-2528-4935-af70-2695047c7b41)

Tela de Login

![image](https://github.com/user-attachments/assets/15325b5d-d4f9-4e66-8e62-fd8793dc8439)

Tela principal

![image](https://github.com/user-attachments/assets/72a07f2b-2f9f-412e-aac7-e22b82138b8b)

Criando uma receita!

![image](https://github.com/user-attachments/assets/22e252b5-c625-4be0-8411-0882c38147ad)

Na pasta do projeto várias coisas foram criadas:
Temos agora a classe DisplayApp.java que é onde as coisas acontecem, e alguns métodos da classe de conexão com o BD também mudaram para se comunicar com a interface gráfica JavaFX (em vez do console).

![image](https://github.com/user-attachments/assets/dbe68def-9323-4902-9414-d6f5419defec)

Além disso criei um arquivo style.css para colocar as alterações nas telas e me aproximar do que foi criado pelo responsável pelo design das telas.

## FOODWISE 1.0
O estado atual do projeto
Tela inicial
![image](https://github.com/user-attachments/assets/45147491-4a60-4567-90ed-6f0a217a0cde)

Registro
![image](https://github.com/user-attachments/assets/8ba09d8c-89f3-407d-9bfd-1015be67cb8e)

Tela principal
![image](https://github.com/user-attachments/assets/17676144-84aa-46bd-8c0d-227140d3fbc3)

Adicionando receitas
![image](https://github.com/user-attachments/assets/951850e0-3737-476d-91c3-ed6826df805e)

Lista de receitas
![image](https://github.com/user-attachments/assets/25c20b5d-2228-4074-a1e7-c52944ba4b69)

Lista de alimentos
![image](https://github.com/user-attachments/assets/38563350-a316-4c78-91da-57c5d62de9d8)

![image](https://github.com/user-attachments/assets/87a714e8-54e2-4a10-89bf-bd7ab9950f74)

Após adicionar o frango eu tenho 1 item perto de vencer. 

![image](https://github.com/user-attachments/assets/88e4a8e6-0a29-49a7-9fc4-369c7975cd75)


















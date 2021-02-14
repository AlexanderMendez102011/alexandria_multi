package login.packet;

import kernel.Main;
import login.LoginClient;
import login.LoginClient.Status;
import object.Account;

class ChooseNickName {

    static void verify(LoginClient client, String nickname) {
        final Account account = client.getAccount();

        if (!account.getPseudo().isEmpty()) {
            client.kick();
            return;
        }

        if (nickname.toLowerCase().equals(account.getName().toLowerCase())) {
            client.send("AlEr");
            return;
        }

        if (!nickname.matches("[A-Za-z0-9.@.-]+") || Main.database.getAccountData().exist(nickname) != null) {
            client.send("AlEs");
            return;
        }

        client.getAccount().setPseudo(nickname);
        client.setStatus(Status.SERVER);
        client.getAccount().setState(0);
        AccountQueue.verify(client);
    }
}

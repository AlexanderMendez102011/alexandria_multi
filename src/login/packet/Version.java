package login.packet;

import kernel.Config;
import login.LoginClient;
import login.LoginClient.Status;

class Version {

    static void verify(LoginClient client, String version) {
        if (!version.equalsIgnoreCase(Config.version)) {
            System.out.println("[" + client.getIoSession().getId() + "] The version of the client '" + version + "' is not like the server '" + Config.version + "'. The client going to be kicked.");
            client.send("AlEv" + Config.version);
            client.kick();
            return;
        }

        client.setStatus(Status.WAIT_ACCOUNT);
    }
}

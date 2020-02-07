


INSERT INTO "oauth_client_details" ("client_id", "resource_ids", "client_secret", "scope", "authorized_grant_types", "web_server_redirect_uri", "authorities", "access_token_validity", "refresh_token_validity", "additional_information", "autoapprove") VALUES
	(E'scpsKerala', E'scpsKerala@123#!', E'pass', E'read, write', E'refresh_token,client_credentials,password', NULL, E'dashboard', 30000, 3000000, NULL, NULL)  ON CONFLICT DO NOTHING;;

	
	
	<h1>Cloudj 1.0 - Note</h1>
	
	<p>L'attuale versione è in grado di utilizzare correttamente mongoDb sia in remoto che locale.</p>
		
		<h2>Remoto</h2>
		<p>La servlet MongoRemote mostra l'utilizzo dei metodi di base della libreria Java per MongoDb</p>
		
		<a href="http://app-cloudj.rhcloud.org/MongoRemote" target="_blank">Chiama MongoRemote</a>
		
		<h2>Locale</h2>
		
		<p>Configurazione di Jboss</p>
		<p>Scaricare il modulo MongoDb per jboss da <a href="https://github.com/openshift/jboss-as7-modules" target="_blank">qui</a>. Ed estrarre la cartella modulese all'interno della cartella mongodb nella root della vostra copia locale di jboss.</p>
		<p>La servlet MongoLocale riproduce lo stesso esempio di MongoRemote ma su una macchian locale in cui sia stato configurata e avviata un'istanza di MongoDb, come nella seguente procedura guidata</p>
		
		<ul>
			<li>Scaricare MongoDb da <a href="">qui</a></li>
			<li>Estrarrre l'archivio (es. in C:\mongodb)</li>
			<li>Creare una directory "data" in C:\mongodb e al suo interno una ulteriore diretory "db".</li>
			<li>Aprire un terminale in C:\mongod\bin</li>
			<li>Lanciare il server mongodDb con il seguente comando:
			<pre>mongod.exe --dbpath=C:\mongo\data --rest --auth --journal --setParameter enableLocalhostAuthBypass=0</pre>
			</li>
			<li>Deployate l'app in locale su jboss e verificate che il db funzioni all'indirizzo http://127.0.0.1:8080/app/MongoLocal</li>
		</ul>
		
		
		<h2>Repository</h2>
		
		<h3>app @ Openshift</h3>
		Configurazione del client.
		
		<h2>app @ Github</h2>
		<a href="https://github.com/dotsperinch/app" target="_blank">link</a>
		
		<p>Procedura per l'allinaemanto del repository Github con Openshift.</p>
		<ul>
		<li><p>Aprire un terminale Git nella cartella del vostro repository locale Github ed eseguire</p></li>
		<li>	
			<pre>git remote add upstream -m master ssh://5255a7fc4382ec6fc5000041@app-cloudj.rhcloud.com/~/git/app.git/ </pre>
			<pre>git pull -s recursive -X theirs upstream master</pre>
			<pre>git push</pre>
			</li>
		</ul>

const util = require('util');
const exec = util.promisify(require('child_process').exec);
const fs = require('fs');


function clean(str)
{
	let regex = /\[.m/;
	let regex2 = /\[..m/;
	let regex3 = /---> Running in .{12}/;
	return str.replace(regex, "")
	          .replace(regex2, "\n")
	          .replace(regex3, "");
}

function writeToFile(path, code)
{
	return new Promise((resolve, reject) =>
	{
		fs.writeFile(path, code, function (err)
		{
			if (err)
			{
				console.log(err);
				reject();
			}
			resolve();
		});
	});
}

async function runCommand(command)
{
	const {stdout, stderr} = await exec(command);
	if (stderr) return stderr + "\n\n" + stdout;
	return stdout;
}

async function compileAndRun_C(code)
{
	try
	{
		//write code Main.cpp
		await writeToFile("src/Docker_C++/Main.cpp", code);
		//create c++ image
		await runCommand("cd src/Docker_C++ && docker build -t cimage . ");
		//run the image and return the console output
		return await runCommand("docker run --rm cimage");
	}
	catch (error)
	{
		//if something went wrong the error will be sent to the client
		let compiler = error.stdout.split(" Main ")[1];
		if (compiler) return clean(compiler);
		return error.message;
	}
}

async function compileAndRun_python(code)
{
	try
	{
		await writeToFile("src/Docker_Python/main.py", code);
		await runCommand("cd src/Docker_Python && docker build -t pythonimage .");
		return await runCommand("docker run --rm --name main pythonimage");
	}
	catch (error)
	{
		return error.stderr;
	}
}

async function compileAndRun_java(code)
{
	try
	{
		await writeToFile("src/Docker_Java/Main.java", code);
		await runCommand("cd src/Docker_Java && docker build -t javaimage .");
		return await runCommand("docker run --rm javaimage");
	}
	catch (error)
	{
		//if something went wrong the error will be sent to the client
		let compiler = error.stdout.split(" javac ")[1];
		if (compiler) return clean(compiler);
		return error.message;
	}
}

async function compileAndRun_rust(code)
{
	try
	{
		await writeToFile("src/Docker_Rust/main.rs", code);
		await runCommand("cd src/Docker_Rust && docker build -t rustimage .");
		return await runCommand("docker run -it --rm --name main rustimage");
	}
	catch (error)
	{
		let compiler = error.stdout.split("javac ")[1];
		if (compiler) return compiler;
		console.log(error);
		return error.message;
	}
}

module.exports = {compileAndRun_C, compileAndRun_python, compileAndRun_java, compileAndRun_rust};
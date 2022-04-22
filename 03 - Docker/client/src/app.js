import './style/App.css';

import OutlinedCard from "./components/components";
import {service} from './services/service';

import React, {useState} from "react";
import {Controlled as CodeMirror} from 'react-codemirror2';
import 'codemirror/lib/codemirror.css';
import 'codemirror/theme/panda-syntax.css';

import {createMuiTheme, ThemeProvider} from '@material-ui/core/styles';
import Button from "@material-ui/core/Button";
import {AccessibleForward} from "@material-ui/icons";
import Box from "@material-ui/core/Box";
import Grid from "@material-ui/core/Grid";
import {orange} from "@material-ui/core/colors";

require('codemirror/mode/clike/clike');

const theme = createMuiTheme({
	palette: {
		primary: orange,
		secondary: {
			main: '#40C4FF',
		}
	}
});

const boxProps = {
	width: 1200,
	marginTop: 3,
	marginBottom: 3,
	border: 2,
	borderRadius: 5,
	borderColor: "primary.main",
	overflow: "hidden"
};

export default function App()
{
	const [output, setOutput] = useState(""); //output from running the code
	const [error, setError] = useState(false); //if server doesnt respons error is used to
	const [waiting, setWaiting] = useState(false); //while waiting for a response we are waiting
	const [currLanguage, setCurrlanguage] = useState(0); //keeps track of what language is currently selected
	const [languages, setLanguages] = useState([
		{
			name: "c++",
			url: "/c",
			id: 0,
			code: "#include <iostream>\n\nint main()\n{\n  for(int i = 0; i < 10; i++)\n  {\n    std::cout << i << \" Hello C++!\" << std::endl;\n  }\n  return 0; \n}"
		},
		{
			name: "python",
			url: "/python",
			id: 1,
			code: "for i in range(10):\n  print(i, \"Hello Python!\")"
		},
		{
			name: "java",
			url: "/java",
			id: 2,
			code: "public class Main\n{\n  public static void main(String[] args)\n  {\n    for(int i = 0; i < 10; i++)\n    {\n      System.out.println(i + \" Hello Java!\");\n    }\n  }\n}"
		},
		// {
		// 	name: "rust",
		// 	url: "/rust",
		// 	id: 3,
		// 	code: "fn main()\n{\n  println(\"Hello Rust!\");\n}"
		// },
	]);

	return (
		<ThemeProvider theme = {theme}>
			<div className = "App">
				<header className = "App-header">
					{renderLanguagePicker()}
					{renderCodeField()}
					{renderCompileAndRunButton()}
					{renderOutputField()}
				</header>
			</div>
		</ThemeProvider>
	);

	function updateCode(code)
	{
		let updated = languages.map(l => l.id === currLanguage ? {...l, code: code} : l);
		setLanguages([...updated]);
	}

	function renderLanguagePicker()
	{
		return (
			<Box marginTop = {3}>
				<Grid container
				      spacing = {3}
				      direction = "row"
				      justify = "center"
				      alignItems = "center">
					{languages.map(lang => (
						<Grid key = {lang.id}
						      item>
							<Button variant = {currLanguage === lang.id ? "contained" : "outlined"}
							        color = "secondary"
							        disableRipple = {currLanguage === lang.id}
							        onClick = {() => setCurrlanguage(lang.id)}>
								{lang.name}
							</Button>
						</Grid>
					))}
				</Grid>
			</Box>
		);
	}

	function renderCodeField()
	{
		return (
			<Box  {...boxProps}>
				<CodeMirror
					value = {languages[currLanguage].code}
					options = {{
						mode: 'clike',
						theme: 'panda-syntax',
						lineNumbers: true
					}}
					onBeforeChange = {(editor, data, value) => updateCode(value)}
				/>
			</Box>
		);
	}

	function renderCompileAndRunButton()
	{
		return (
			<Box width = {200}>
				<Button variant = {error || waiting ? "contained" : "outlined"}
				        color = {error ? "primary" : "secondary"}
				        fullWidth
				        disableRipple = {error || waiting}
				        disableTouchRipple = {error}
				        startIcon = {<AccessibleForward/>}
				        onClick = {() => compileAndRun()}>
					{error ? "Try Again" : waiting ? "Running Code" : "Run Code"}
				</Button>
			</Box>
		);
	}

	function compileAndRun()
	{
		if (waiting) return; //still waiting for response from server

		setOutput("");
		setError(false);
		setWaiting(true);

		let timeout = true;
		setTimeout(function ()
		{
			if (timeout)
			{
				setWaiting(false);
				setError(true);
				setOutput("The server timed out!\n\nMake sure the server is running then try again!");
			}
		}, 4000);

		service.run(languages[currLanguage]).then(output =>
		{
			timeout = false;
			setOutput(output);
			setWaiting(false);
		});
	}

	function renderOutputField()
	{
		return (
			<>
				<Box  {...boxProps}>
					{OutlinedCard(output, waiting)}
				</Box>
			</>
		);
	}
}

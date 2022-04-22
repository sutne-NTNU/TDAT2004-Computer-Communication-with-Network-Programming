import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CircularProgress from "@material-ui/core/CircularProgress";
import Typography from "@material-ui/core/Typography";


const cardStyle = makeStyles({
	root: {
		minHeight: 340,
		textAlign: 'left',
		fontSize: 12,
		background: '#2E2E2E',
		color: '#E8E8E8',
		justify: 'center'
	},
	waiting: {
		minHeight: 340,
		background: '#2E2E2E',
	}
});

const progressStyle = makeStyles({
	root: {
		color: 'rgba(58,156,239,0.41)',
		marginTop: 35,
	}
});


export default function OutlinedCard(text, waiting)
{
	const cards = cardStyle();
	const progress = progressStyle();
	if (waiting)
	{
		return (
			<>
				<Card
					className = {cards.waiting}
					variant = "outlined">
					<CardContent>
						<CircularProgress className = {progress.root}
						                  size = {220}/>
					</CardContent>
				</Card>

			</>
		);
	}
	return (

		<Card className = {cards.root}
		      variant = "outlined">
			<CardContent>
				<Typography>
					{text}
				</Typography>
			</CardContent>
		</Card>
	);
}
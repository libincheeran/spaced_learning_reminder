import { Button } from "@material-ui/core";
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';
import React, { useState, useEffect } from 'react'
import { useAlert } from 'react-alert'

const LcProblemOverdue = ({ toggle, setRender }) => {

  const alert = useAlert()

  const useStyles = makeStyles({
    table: {
      minWidth: 650,
      alignItems: 'center'
    },
    button: {
      justifyContent: 'center'
    }
  });

  const classes = useStyles();

  const [problems, setProblem] = useState([]);
  // const [reRender, setRender] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8080/api/lcProblem/overdue")
      .then((res) => res.json())
      .then((problems) => {
        setProblem(problems);
      });
    console.log("component mounted")
  }, [toggle])


  const handleDeleteClick = (row) => {
    const id = row.id;
    const requestOptions = {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' }
    };
    const url = `http://localhost:8080/api/lcProblem/delete/${id}`
    fetch(url, requestOptions)
      .then(res => {
        console.log(res);
        if (res.status !== 200) {
          alert.error(`Failed to delete record ${id}`)
        } else {
          setRender(!toggle);
          alert.success(`Deleted record ${row.problemId}`)
        }
      })
  }


  return (
    <div>
      <h2> Overdue Problems
      &nbsp;&nbsp;&nbsp;
      {/* <Button variant="outlined" color="primary" onClick={() => setRender(!reRender)}>Refresh</Button> */}
        <br></br>
      </h2>
      <TableContainer component={Paper}>
        <Table className={classes.table} aria-label="simple table">
          <TableBody>
            {problems.map((row) => (
              <TableRow key={row.id}>
                <TableCell align="left">{row.problemId}</TableCell>
                <TableCell align="center">{row.description}</TableCell>

                <Button size="small" variant="contained" color="primary" onClick={() => handleDeleteClick(row)}>Done</Button>
&nbsp;&nbsp;&nbsp;
                <Button size="small" variant="contained" color="secondary" onClick={() => handleDeleteClick(row)}>Delete</Button>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div >
  )
}

export default LcProblemOverdue;
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import React, { useState, useEffect } from 'react'
import { useAlert } from 'react-alert'

const LcProblemOverdue = ({ toggle, setRender }) => {

  const alert = useAlert()

  const StyledTable = styled(Table)({
    minWidth: 650,
    alignItems: 'center'
  });

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
        <StyledTable aria-label="simple table">
          <TableBody>
            {problems.map((row) => (
              <TableRow key={row.id}>
                <TableCell align="left">{row.problemId}</TableCell>
                <TableCell align="center">{row.description}</TableCell>
                <TableCell>
                  <Button size="small" variant="contained" color="primary" onClick={() => handleDeleteClick(row)}>Done</Button>
                  &nbsp;&nbsp;&nbsp;
                  <Button size="small" variant="contained" color="error" onClick={() => handleDeleteClick(row)}>Delete</Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </StyledTable>
      </TableContainer>
    </div >
  )
}

export default LcProblemOverdue;
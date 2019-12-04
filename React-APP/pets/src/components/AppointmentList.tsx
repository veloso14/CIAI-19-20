import React from 'react';
import {Appointment} from "./PetList";
import {GlobalState} from "../App";
import {connect} from "react-redux";
import ListGroup from "react-bootstrap/ListGroup";
import {Link} from "react-router-dom";

const AppointmentList = (props: { appointments: Appointment[] }) => {
    return (
        <div>
            <h1 className="text-center">Appointments list</h1>
            <br/>
            <ListGroup>{props.appointments.map((apt: Appointment) => <ListGroup.Item
                key={apt.id}><Link to={`/appointment/${apt.id}`}>{apt.desc}</Link></ListGroup.Item>)}</ListGroup>
        </div>
    );
};


export default AppointmentList;